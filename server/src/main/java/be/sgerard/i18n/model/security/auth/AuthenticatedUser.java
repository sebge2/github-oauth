package be.sgerard.i18n.model.security.auth;

import be.sgerard.i18n.model.security.user.dto.UserDto;
import be.sgerard.i18n.service.security.UserRole;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * {@link UserDto User} authenticated, or about to be authenticated on the platform.
 *
 * @author Sebastien Gerard
 */
public interface AuthenticatedUser extends AuthenticatedPrincipal, Serializable {

    /**
     * {@link GrantedAuthority Authority} that every user has.
     */
    GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");

    /**
     * Returns the unique id of the authenticated user, different from the user's id. Only related to the current
     * authentication.
     */
    String getId();

    /**
     * Returns the id of the associated {@link be.sgerard.i18n.model.security.user.persistence.UserEntity user}.
     */
    String getUserId();

    /**
     * Returns the {@link UserRole roles} attributed to the user.
     *
     * @see #getAuthorities()
     */
    Collection<UserRole> getRoles();

    /**
     * Returns all the {@link GrantedAuthority authorities} of this user.
     *
     * @see #getRoles()
     * @see UserRole#toAuthority()
     */
    Collection<? extends GrantedAuthority> getAuthorities();

    /**
     * Returns the {@link RepositoryCredentials credentials} to use for the specified repository.
     */
    <A extends RepositoryCredentials> Optional<A> getCredentials(String repository, Class<A> expectedType);

    /**
     * Returns all the available {@link RepositoryCredentials credentials}.
     */
    Collection<RepositoryCredentials> getRepositoryCredentials();

    /**
     * Updates {@link #getRoles() roles}.
     */
    AuthenticatedUser updateRoles(List<UserRole> sessionRoles);

    /**
     * Updates credentials of a repository.
     */
    AuthenticatedUser updateRepositoryCredentials(RepositoryCredentials repositoryCredentials);
}
