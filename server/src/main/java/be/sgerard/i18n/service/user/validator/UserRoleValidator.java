package be.sgerard.i18n.service.user.validator;

import be.sgerard.i18n.model.security.user.dto.UserCreationDto;
import be.sgerard.i18n.model.security.user.persistence.UserEntity;
import be.sgerard.i18n.model.security.user.dto.UserPatchDto;
import be.sgerard.i18n.model.validation.ValidationMessage;
import be.sgerard.i18n.model.validation.ValidationResult;
import org.springframework.stereotype.Component;

/**
 * @author Sebastien Gerard
 */
@Component
public class UserRoleValidator implements UserValidator {

    public UserRoleValidator() {
    }

    @Override
    public ValidationResult validateOnCreate(UserCreationDto info) {
        return info.getRoles().stream()
                .filter(role -> !role.isAssignableByEndUser())
                .map(role -> ValidationResult.builder().messages(new ValidationMessage("ROLE_UN_ASSIGNABLE", role.name())).build())
                .collect(ValidationResult.toValidationResult());
    }

    @Override
    public ValidationResult validateOnUpdate(UserPatchDto userUpdate, UserEntity userEntity) {
        return userUpdate.getRoles()
                .map(roles -> roles.stream()
                        .filter(role -> !role.isAssignableByEndUser())
                        .map(role -> ValidationResult.builder().messages(new ValidationMessage("ROLE_UN_ASSIGNABLE", role.name())).build())
                        .collect(ValidationResult.toValidationResult())
                )
                .orElse(ValidationResult.EMPTY);
    }
}
