package be.sgerard.i18n.model.repository.dto;

import be.sgerard.i18n.model.repository.RepositoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Optional;

/**
 * Request asking the update of a Git repository.
 *
 * @author Sebastien Gerard
 */
@ApiModel(value = "GitRepositoryPatchRequest", description = "Request asking the update of a Git repository")
@JsonDeserialize(builder = GitRepositoryPatchDto.Builder.class)
public class GitRepositoryPatchDto extends RepositoryPatchDto {

    public static Builder gitBuilder() {
        return new Builder();
    }

    @ApiModelProperty(notes = "The default branch to use", required = true)
    private final String defaultBranch;

    protected GitRepositoryPatchDto(BaseBuilder<?, ?> builder) {
        super(builder);

        this.defaultBranch = builder.defaultBranch;
    }

    @Override
    public RepositoryType getType() {
        return RepositoryType.GIT;
    }

    /**
     * Returns the default branch to use.
     */
    public Optional<String> getDefaultBranch() {
        return Optional.ofNullable(defaultBranch);
    }

    /**
     * Builder of {@link GitRepositoryPatchDto GIT repository patch DTO}.
     */
    public static abstract class BaseBuilder<R extends GitRepositoryPatchDto, B extends GitRepositoryPatchDto.BaseBuilder<R, B>> extends RepositoryPatchDto.BaseBuilder<R, B> {

        private String defaultBranch;

        protected BaseBuilder() {
        }

        public B defaultBranch(String defaultBranch) {
            this.defaultBranch = defaultBranch;
            return self();
        }
    }

    /**
     * Builder of {@link GitRepositoryPatchDto GIT repository patch DTO}.
     */
    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder extends GitRepositoryDto.BaseBuilder<GitRepositoryDto, GitRepositoryDto.Builder> {

        public Builder() {
        }

        @Override
        public GitRepositoryDto build() {
            return new GitRepositoryDto(this);
        }
    }
}