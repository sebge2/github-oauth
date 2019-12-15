package be.sgerard.i18n.client.github;

import be.sgerard.i18n.controller.AbstractControllerTest;
import be.sgerard.i18n.model.github.GitHubPullRequestDto;
import be.sgerard.i18n.model.repository.dto.GitHubRepositoryDto;
import be.sgerard.test.i18n.support.WithInternalUser;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static be.sgerard.test.i18n.model.GitRepositoryCreationDtoTestUtils.i18nToolRepositoryCreationDto;
import static be.sgerard.test.i18n.model.UserDtoTestUtils.userJohnDoeCreation;

/**
 * @author Sebastien Gerard
 */
public class GitHubPullRequestClientTest extends AbstractControllerTest {
//
//    @Autowired
//    private GitHubPullRequestClient manager;
//
//    @Before
//    public void setup() throws Exception {
//        user.createUser(userJohnDoeCreation().build());
//    }
//
//    @Test
//    @Transactional
//    @WithInternalUser(roles = { "ADMIN"})
//    public void findAll() throws Exception {
//        final GitHubRepositoryDto repository = this.repository.create(i18nToolRepositoryCreationDto(), GitHubRepositoryDto.class).get();
//
//        final List<GitHubPullRequestDto> byNumber = manager.findAll(repository.getId()).collectList().block();
//        System.out.println(
//                objectMapper.writeValueAsString(byNumber)
//
//        );
//    }

}
