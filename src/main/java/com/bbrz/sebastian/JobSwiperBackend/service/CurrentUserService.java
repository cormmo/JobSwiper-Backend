package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.exception.ForbiddenOperationException;
import com.bbrz.sebastian.JobSwiperBackend.exception.ResourceNotFoundException;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Provides access to the currently authenticated user.
 *
 * <p>Also verifies whether the current user has a required role.</p>
 */
@Service
public class CurrentUserService {

    private final UserAccountRepository users;

    /**
     * Creates the current user service.
     *
     * @param users user repository
     */
    public CurrentUserService(UserAccountRepository users) {
        this.users = users;
    }

    /**
     * Returns the currently authenticated and active user.
     *
     * @param authentication current authentication
     * @return authenticated user
     */
    public UserAccount require(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenOperationException("Authentication is required");
        }

        return users.findByUsernameIgnoreCase(authentication.getName())
                .filter(UserAccount::isActive)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user no longer exists"
                        ));
    }

    /**
     * Returns the current user if they have the required role.
     *
     * @param authentication current authentication
     * @param role required role
     * @return authenticated user
     */
    public UserAccount requireRole(Authentication authentication, Role role) {
        UserAccount user = require(authentication);

        if (user.getRole() != role) {
            throw new ForbiddenOperationException(
                    "This operation requires role " + role
            );
        }

        return user;
    }
}