package com.bbrz.sebastian.JobSwiperBackend.api;

import com.bbrz.sebastian.JobSwiperBackend.dto.AuthDtos;
import com.bbrz.sebastian.JobSwiperBackend.service.AuthService;
import com.bbrz.sebastian.JobSwiperBackend.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Provides authentication-related API endpoints.
 *
 * <p>Handles user registration, login, current user information and logout.</p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUsers;

    /**
     * Creates the authentication controller.
     *
     * @param authService service for registration and login
     * @param currentUsers service for accessing the current user
     */
    public AuthController(AuthService authService, CurrentUserService currentUsers) {
        this.authService = authService;
        this.currentUsers = currentUsers;
    }

    /**
     * Registers a new user account.
     *
     * @param request registration data
     * @return authentication response for the new user
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDtos.AuthResponse register(
            @Valid @RequestBody AuthDtos.RegisterRequest request) {
        return authService.register(request);
    }

    /**
     * Authenticates a user and returns a JWT.
     *
     * @param request login credentials
     * @return authentication response
     */
    @PostMapping("/login")
    public AuthDtos.AuthResponse login(
            @Valid @RequestBody AuthDtos.LoginRequest request) {
        return authService.authenticate(request);
    }

    /**
     * Returns information about the currently authenticated user.
     *
     * @param authentication current authentication
     * @return current user information
     */
    @GetMapping("/me")
    public AuthDtos.UserResponse me(Authentication authentication) {
        return AuthDtos.UserResponse.from(currentUsers.require(authentication));
    }

    /**
     * Logs out the current user.
     *
     * <p>Because JWT authentication is stateless, logout is handled by
     * removing the token on the client.</p>
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        // Stateless JWT logout is performed by discarding the token on the client.
    }
}