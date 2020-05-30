package be.sgerard.i18n.service.workspace;

import be.sgerard.i18n.model.i18n.WorkspaceStatus;
import be.sgerard.i18n.model.i18n.persistence.WorkspaceEntity;
import be.sgerard.i18n.model.repository.persistence.RepositoryEntity;
import be.sgerard.i18n.repository.i18n.WorkspaceRepository;
import be.sgerard.i18n.service.LockTimeoutException;
import be.sgerard.i18n.service.ResourceNotFoundException;
import be.sgerard.i18n.service.ValidationException;
import be.sgerard.i18n.service.i18n.TranslationManager;
import be.sgerard.i18n.service.workspace.listener.WorkspaceListener;
import be.sgerard.i18n.service.i18n.review.WorkspaceReviewHandler;
import be.sgerard.i18n.service.repository.RepositoryException;
import be.sgerard.i18n.service.repository.RepositoryManager;
import be.sgerard.i18n.support.ReactiveUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Implementation of the {@link WorkspaceManager workspace manager}.
 *
 * @author Sebastien Gerard
 */
@Service
public class WorkspaceManagerImpl implements WorkspaceManager {

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceManagerImpl.class);

    private final WorkspaceRepository repository;
    private final RepositoryManager repositoryManager;
    private final TranslationManager translationManager;
    private final WorkspaceListener listener;
    private final WorkspaceReviewHandler reviewHandler;

    public WorkspaceManagerImpl(WorkspaceRepository repository,
                                RepositoryManager repositoryManager,
                                TranslationManager translationManager,
                                WorkspaceListener listener,
                                WorkspaceReviewHandler reviewHandler) {
        this.repositoryManager = repositoryManager;
        this.translationManager = translationManager;
        this.repository = repository;
        this.listener = listener;
        this.reviewHandler = reviewHandler;
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<WorkspaceEntity> findAll() {
        return Flux.fromStream(repository.findAll().stream());
    }

    @Override
    public Flux<WorkspaceEntity> findAll(String repositoryId) {
        return Flux.fromStream(repository.findByRepositoryId(repositoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<WorkspaceEntity> findById(String id) {
        return Mono.justOrEmpty(repository.findById(id));
    }

    @Override
    @Transactional
    public Flux<WorkspaceEntity> synchronize(String repositoryId) throws RepositoryException {
        return repositoryManager
                .applyOnRepository(repositoryId, api ->
                        ReactiveUtils
                                .combine(api.listBranches(), findAll(repositoryId), (branch, workspace) -> branch.compareTo(workspace.getBranch()))
                                .flatMap(pair -> {
                                    if (pair.getRight() == null) {
                                        return repositoryManager
                                                .findByIdOrDie(repositoryId)
                                                .map(repository -> createWorkspace(repository, pair.getLeft()));
                                    } else {
                                        switch (pair.getRight().getStatus()) {
                                            case IN_REVIEW:
                                                if (reviewHandler.isReviewFinished(pair.getRight())) {
                                                    return doFinishReview(pair.getRight());
                                                }

                                                return Mono.just(pair.getRight());
                                            case INITIALIZED:
                                            case NOT_INITIALIZED:
                                                if (pair.getLeft() == null) {
                                                    return delete(pair.getRight().getId()).flatMap(v -> Mono.empty());
                                                } else {
                                                    return Mono.just(pair.getRight());
                                                }
                                            default:
                                                return Flux.error(new UnsupportedOperationException("Unsupported workspace status [" + pair.getRight().getStatus() + "]."));
                                        }
                                    }
                                })
                )
                .flatMapMany(flux -> flux);
    }

    @Override
    @Transactional
    public Mono<WorkspaceEntity> initialize(String workspaceId) throws LockTimeoutException, RepositoryException {
        return findByIdOrDie(workspaceId)
                .flatMap(workspace -> {
                    if (workspace.getStatus() == WorkspaceStatus.INITIALIZED) {
                        return Mono.just(workspace);
                    }

                    ValidationException.throwIfFailed(listener.beforeInitialize(workspace));

                    logger.info("Initialing workspace {}.", workspaceId);

                    return repositoryManager
                            .consumeRepository(workspace.getRepository().getId(), api -> translationManager.readTranslations(workspace, api))
                            .then(Mono.fromSupplier(() -> {
                                workspace.setStatus(WorkspaceStatus.INITIALIZED);

                                listener.onInitialize(workspace);

                                return workspace;
                            }));
                });
    }

    @Override
    @Transactional
    public Mono<WorkspaceEntity> publish(String workspaceId, String message) throws ResourceNotFoundException, LockTimeoutException, RepositoryException {
        return findByIdOrDie(workspaceId)
                .flatMap(workspace -> {
                    if (workspace.getStatus() == WorkspaceStatus.IN_REVIEW) {
                        return Mono.just(workspace);
                    }

                    ValidationException.throwIfFailed(listener.beforePublish(workspace));

                    logger.info("Start publishing workspace {}.", workspaceId);

                    if(reviewHandler.isReviewSupported(workspace)){
                        workspace.setStatus(WorkspaceStatus.IN_REVIEW);

                        listener.onReview(workspace);

                        return Mono.just(workspace);
                    } else {


                        return delete(workspace.getId())
                                .thenReturn(createWorkspace(workspace.getRepository(), workspace.getBranch()));
                    }
                });
    }

    @Override
    @Transactional
    public Mono<WorkspaceEntity> finishReview(String workspaceId) throws ResourceNotFoundException, RepositoryException {
        return findById(workspaceId)
                .flatMap(this::doFinishReview);
    }

    @Override
    @Transactional
    public Mono<Void> updateTranslations(String workspaceId, Map<String, String> translations) throws ResourceNotFoundException {
        final WorkspaceEntity workspace = repository.findById(workspaceId)
                .orElseThrow(() -> ResourceNotFoundException.workspaceNotFoundException(workspaceId));

        if (workspace.getStatus() != WorkspaceStatus.INITIALIZED) {
            throw new IllegalStateException("Cannot update translations of workspace [" + workspaceId + "], the status "
                    + workspace.getStatus() + " does not allow it.");
        }

        translationManager.updateTranslations(workspace, translations);

        return Mono.empty();
    }

    @Override
    @Transactional
    public Mono<Void> delete(String workspaceId) throws RepositoryException, LockTimeoutException {
        final WorkspaceEntity workspaceEntity = repository.findById(workspaceId).orElse(null);

        if (workspaceEntity != null) {
//            final String pullRequestBranch = workspaceEntity.getPullRequestBranch().orElse(null);
            final String pullRequestBranch = "";
//            if (pullRequestBranch != null) {
//                repositoryManager.open(api -> {
//                    logger.info("The branch {} has been removed.", pullRequestBranch);
//
//                    api.removeBranch(pullRequestBranch);
//                });
//            }

            listener.onDelete(workspaceEntity);

            logger.info("The workspace {} has been deleted.", workspaceId);

            repository.delete(workspaceEntity);
        }

        return Mono.empty();
    }

    /**
     * Creates a new {@link WorkspaceEntity workspace} based on the specified branch.
     */
    private WorkspaceEntity createWorkspace(RepositoryEntity repository, String branch) {
        final WorkspaceEntity workspaceEntity = new WorkspaceEntity(repository, branch);

        listener.onCreate(workspaceEntity);

        logger.info("The workspace {} has been created.", workspaceEntity);

        this.repository.save(workspaceEntity);

        return workspaceEntity;
    }

    /**
     * Terminates the review on the specified workspace.
     */
    private Mono<WorkspaceEntity> doFinishReview(WorkspaceEntity workspace) throws LockTimeoutException, RepositoryException {
        ValidationException.throwIfFailed(listener.beforeFinishReview(workspace));

        logger.info("The review is now finished, deleting the workspace {} and then creates a new one.", workspace.getId());

        return delete(workspace.getId())
                .thenReturn(createWorkspace(workspace.getRepository(), workspace.getBranch()));
    }

//    private String generateUniqueBranch(String name, GitAPI api) {
//        if (!api.listRemoteBranches().contains(name) && !api.listLocalBranches().contains(name)) {
//            return name;
//        }
//
//        String generatedName;
//        int index = 0;
//        do {
//            generatedName = name + "_" + (++index);
//        } while (api.listRemoteBranches().contains(generatedName) || api.listLocalBranches().contains(generatedName));
//
//        return generatedName;
//    }
}