package be.sgerard.i18n.controller;

import be.sgerard.i18n.model.repository.dto.GitHubRepositoryDto;
import be.sgerard.i18n.model.repository.dto.GitRepositoryDto;
import be.sgerard.i18n.model.repository.dto.RepositoryDto;
import be.sgerard.i18n.model.workspace.WorkspaceDto;
import be.sgerard.i18n.model.workspace.WorkspaceStatus;
import be.sgerard.test.i18n.support.TransactionalReactiveTest;
import be.sgerard.test.i18n.support.WithInternalUser;
import org.junit.jupiter.api.*;

import java.util.Locale;

import static be.sgerard.test.i18n.model.GitRepositoryCreationDtoTestUtils.i18nToolLocalRepositoryCreationDto;
import static be.sgerard.test.i18n.model.GitRepositoryCreationDtoTestUtils.i18nToolRepositoryCreationDto;
import static be.sgerard.test.i18n.model.TranslationLocaleCreationDtoTestUtils.enLocaleCreationDto;
import static be.sgerard.test.i18n.model.TranslationLocaleCreationDtoTestUtils.frLocaleCreationDto;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Sebastien Gerard
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkspaceControllerTest extends AbstractControllerTest {

    @BeforeAll
    public void setupRepo() throws Exception {
        gitRepo
                .createMockFor(i18nToolRepositoryCreationDto())
                .allowAnonymousRead()
                .onCurrentGitProject()
                .create()
                .createBranches("release/2020.05", "release/2020.06");

        gitRepo
                .createMockFor(i18nToolLocalRepositoryCreationDto())
                .allowAnonymousRead()
                .onCurrentGitProject()
                .create()
                .createBranches("release/2020.05", "release/2020.06");
    }

    @BeforeEach
    public void setupLocales() {
        locale
                .createLocale(frLocaleCreationDto()).and()
                .createLocale(enLocaleCreationDto());
    }

    @AfterEach
    public void clear(){
        // TODO workaround for TransactionInvocationInterceptor
    }

    @AfterAll
    public void destroy() {
        gitRepo.destroyAll();
    }

    @Test
    @TransactionalReactiveTest
    @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
    public void findAll() {
        this.repository
                .create(i18nToolLocalRepositoryCreationDto())
                .initialize()
                .workspaces()
                .sync();

        webClient
                .get()
                .uri("/api/repository/workspace")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(3));
    }

    @Test
    @TransactionalReactiveTest
    @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
    public void findAllOfRepository() {
        final RepositoryDto repository = this.repository
                .create(i18nToolLocalRepositoryCreationDto())
                .initialize()
                .workspaces()
                .sync()
                .getRepo();

        webClient
                .get()
                .uri("/api/repository/{id}/workspace", repository.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").value(hasSize(3));
    }

    @Test
    @TransactionalReactiveTest
    @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
    public void findById() {
        final WorkspaceDto masterWorkspace = repository
                .create(i18nToolRepositoryCreationDto())
                .initialize()
                .workspaces()
                .sync()
                .workspaceForBranch("master")
                .get();

        webClient
                .get()
                .uri("/api/repository/workspace/{id}", masterWorkspace.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.branch").isEqualTo("master")
                .jsonPath("$.status").isEqualTo(WorkspaceStatus.NOT_INITIALIZED.name());
    }

    @Nested
    @DisplayName("GitHub")
    class GitHub extends AbstractControllerTest {

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void synchronize() {
            final GitHubRepositoryDto repository = this.repository.create(i18nToolRepositoryCreationDto(), GitHubRepositoryDto.class).initialize().get();

            webClient
                    .post()
                    .uri("/api/repository/{id}/workspace/do?action=SYNCHRONIZE", repository.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").value(hasSize(3));
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void initialize() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolRepositoryCreationDto())
                    .hint("my-repo")
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .get();

            webClient
                    .post()
                    .uri("/api/repository/workspace/{id}/do?action=INITIALIZE", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.branch").isEqualTo("master")
                    .jsonPath("$.status").isEqualTo(WorkspaceStatus.INITIALIZED.name());

            translations
                    .forRepositoryHint("my-repo")
                    .forWorkspaceName("master")
                    .expectTranslation("ResourceNotFoundException.user.message", Locale.ENGLISH, "There is no user with reference [{0}].")
                    .expectTranslation("SHARED.WORKSPACES_TITLE", Locale.ENGLISH, "Workspaces")
                    .expectNoModification();
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void publish() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolRepositoryCreationDto())
                    .hint("my-repo")
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .initialize()
                    .get();

            webClient
                    .post()
                    .uri("/api/repository/workspace/{id}/do?action=PUBLISH&message=test", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.branch").isEqualTo("master")
                    .jsonPath("$.status").isEqualTo(WorkspaceStatus.IN_REVIEW.name());

            // TODO assert modifications

            repository
                    .forHint("my-repo")
                    .gitHub()
                    .assertHasPullRequests(); // TODO assert PR
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void deleteInitialized() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolRepositoryCreationDto(), GitHubRepositoryDto.class)
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .initialize()
                    .get();

            webClient
                    .delete()
                    .uri("/api/repository/workspace/{id}", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isNoContent();
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void deletePublished() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolRepositoryCreationDto(), GitHubRepositoryDto.class)
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .initialize()
                    .publish("publish message")
                    .get();

            webClient
                    .delete()
                    .uri("/api/repository/workspace/{id}", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isNoContent();
        }
    }

    @Nested
    @DisplayName("Git")
    class Git extends AbstractControllerTest {

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void synchronize() {
            final GitRepositoryDto repository = this.repository.create(i18nToolLocalRepositoryCreationDto(), GitRepositoryDto.class).initialize().get();

            webClient
                    .post()
                    .uri("/api/repository/{id}/workspace/do?action=SYNCHRONIZE", repository.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").value(hasSize(3));

            webClient
                    .get()
                    .uri("/api/repository/workspace")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").value(hasSize(3));
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void initialize() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolLocalRepositoryCreationDto(), GitRepositoryDto.class)
                    .hint("my-repo")
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .get();

            webClient
                    .post()
                    .uri("/api/repository/workspace/{id}/do?action=INITIALIZE", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.branch").isEqualTo("master")
                    .jsonPath("$.status").isEqualTo(WorkspaceStatus.INITIALIZED.name());

            translations
                    .forRepositoryHint("my-repo")
                    .forWorkspaceName("master")
                    .expectTranslation("ResourceNotFoundException.user.message", Locale.ENGLISH, "There is no user with reference [{0}].")
                    .expectTranslation("SHARED.WORKSPACES_TITLE", Locale.ENGLISH, "Workspaces")
                    .expectNoModification();
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void publish() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolLocalRepositoryCreationDto(), GitRepositoryDto.class)
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .initialize()
                    .get();

            webClient
                    .post()
                    .uri("/api/repository/workspace/{id}/do?action=PUBLISH&message=test", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.branch").isEqualTo("master")
                    .jsonPath("$.status").isEqualTo(WorkspaceStatus.NOT_INITIALIZED.name());
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void deleteInitialized() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolLocalRepositoryCreationDto())
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .initialize()
                    .get();

            webClient
                    .delete()
                    .uri("/api/repository/workspace/{id}", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isNoContent();
        }

        @Test
        @TransactionalReactiveTest
        @WithInternalUser(roles = {"MEMBER_OF_ORGANIZATION", "ADMIN"})
        public void deletePublished() {
            final WorkspaceDto masterWorkspace = repository
                    .create(i18nToolLocalRepositoryCreationDto())
                    .initialize()
                    .workspaces()
                    .sync()
                    .workspaceForBranch("master")
                    .initialize()
                    .publish("test message")
                    .get();

            webClient
                    .delete()
                    .uri("/api/repository/workspace/{id}", masterWorkspace.getId())
                    .exchange()
                    .expectStatus().isNoContent();
        }
    }

}
