package be.sgerard.test.i18n.support.auth.external.github;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static be.sgerard.test.i18n.model.UserDtoTestUtils.*;

/**
 * @author Sebastien Gerard
 */
@Retention(RetentionPolicy.RUNTIME)
@WithGitHubExternalUser(username = JANE_DOE_USERNAME, token = GARRICK_KLEIN_TOKEN, email = GARRICK_KLEIN_EMAIL, displayName = GARRICK_KLEIN, roles = {"ADMIN"})
public @interface WithGarrickKleinAdminUser {
}
