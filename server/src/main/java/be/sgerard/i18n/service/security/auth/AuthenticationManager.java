package be.sgerard.i18n.service.security.auth;

import be.sgerard.i18n.model.security.auth.AuthenticatedUser;
import be.sgerard.i18n.model.security.auth.ExternalKeyAuthenticatedUser;
import be.sgerard.i18n.model.security.auth.ExternalOAuth2AuthenticatedUser;
import be.sgerard.i18n.model.security.auth.InternalAuthenticatedUser;
import be.sgerard.i18n.model.security.user.ExternalUserDto;
import be.sgerard.i18n.model.security.user.ExternalUserEntity;
import be.sgerard.i18n.model.security.user.InternalUserEntity;
import be.sgerard.i18n.model.security.user.UserEntity;
import org.springframework.security.access.AccessDeniedException;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Sebastien Gerard
 */
public interface AuthenticationManager {

    ExternalOAuth2AuthenticatedUser initExternalOAuthUser(ExternalUserEntity currentUser, ExternalUserDto externalUserDto);

    ExternalKeyAuthenticatedUser initExternalKeyUser(ExternalUserEntity currentUser, ExternalUserDto externalUserDto);

    InternalAuthenticatedUser initInternalUser(InternalUserEntity currentUser);

    Optional<AuthenticatedUser> getCurrentAuthenticatedUser();

    default AuthenticatedUser getCurrentAuthenticatedUserOrFail() throws AccessDeniedException {
        return getCurrentAuthenticatedUser()
                .orElseThrow(() -> new AccessDeniedException("Please authenticate."));
    }

    UserEntity getCurrentUserOrFail() throws AccessDeniedException;

    UserEntity getUserFromPrincipal(Principal principal);

}
