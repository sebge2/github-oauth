package be.sgerard.i18n.controller;

import be.sgerard.i18n.model.security.auth.AuthenticatedUser;
import be.sgerard.i18n.model.security.user.dto.AuthenticatedUserDto;
import be.sgerard.i18n.service.ResourceNotFoundException;
import be.sgerard.i18n.service.security.auth.AuthenticationManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling authentication.
 *
 * @author Sebastien Gerard
 */
@RestController
@RequestMapping(path = "/api")
@Tag(name = "Authentication", description = "Controller handling authentication (current user, or not).")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Retrieves the current authenticated user.
     */
    @GetMapping("/authentication/user")
    @Operation(summary = "Retrieves the current authenticated user.")
    public AuthenticatedUserDto getCurrentUser() {
        final AuthenticatedUser authenticatedUser = authenticationManager.getCurrentUser()
                .orElseThrow(() -> ResourceNotFoundException.userNotFoundException("current"));

        return AuthenticatedUserDto.builder(authenticatedUser).build();
    }
}
