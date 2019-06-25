package be.sgerard.i18n.model.security.user;

import be.sgerard.i18n.service.security.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static java.util.Collections.unmodifiableSet;

/**
 * @author Sebastien Gerard
 */
@ApiModel(description = "The update of a user.")
@JsonDeserialize(builder = UserUpdateDto.Builder.class)
public class UserUpdateDto {

    public static Builder builder() {
        return new Builder();
    }

    private final String username;
    private final String email;
    private final String password;
    private final String avatarUrl;
    private final Collection<UserRole> roles;

    private UserUpdateDto(Builder builder) {
        roles = builder.roles;
        username = builder.username;
        email = builder.email;
        password = builder.password;
        avatarUrl = builder.avatarUrl;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getAvatarUrl() {
        return Optional.ofNullable(avatarUrl);
    }

    public Optional<Collection<UserRole>> getRoles() {
        return Optional.ofNullable(roles);
    }

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {

        private String username;
        private String email;
        private String password;
        private String avatarUrl;
        private Collection<UserRole> roles;

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder roles(Collection<UserRole> roles) {
            this.roles = unmodifiableSet(new HashSet<>(roles));
            return this;
        }

        public UserUpdateDto build() {
            return new UserUpdateDto(this);
        }
    }
}
