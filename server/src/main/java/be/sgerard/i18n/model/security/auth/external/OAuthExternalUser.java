package be.sgerard.i18n.model.security.auth.external;

import be.sgerard.i18n.model.security.user.persistence.ExternalAuthClient;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * User coming from OAuth.
 *
 * @author Sebastien Gerard
 */
public class OAuthExternalUser {

    private final ExternalAuthClient oauthClient;
    private final String token;
    private final Map<String, Object> attributes;

    @SuppressWarnings("Java9CollectionFactory")
    public OAuthExternalUser(ExternalAuthClient oauthClient, String token, Map<String, Object> attributes) {
        this.oauthClient = oauthClient;
        this.token = token;
        this.attributes = unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Returns the name of the OAuth client (used in the application configuration files).
     */
    public ExternalAuthClient getOauthClient() {
        return oauthClient;
    }

    /**
     * Returns the OAuth token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns user's attributes.
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}