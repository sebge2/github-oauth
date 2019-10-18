package be.sgerard.i18n.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Sebastien Gerard
 */
public enum UserRole {

    MEMBER_OF_ORGANIZATION,

    REPO_MEMBER,

    ADMIN;

    public static final String ROLE_PREFIX = "ROLE_";

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(ROLE_PREFIX + name());
    }
}
