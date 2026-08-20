package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.exception.ForbiddenOperationException;
import com.bbrz.sebastian.JobSwiperBackend.exception.ResourceNotFoundException;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserAccountRepository users;

    public CurrentUserService(UserAccountRepository users) { this.users = users; }

    public UserAccount require(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenOperationException("Authentication is required");
        }
        return users.findByUsernameIgnoreCase(authentication.getName())
                .filter(UserAccount::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user no longer exists"));
    }

    public UserAccount requireRole(Authentication authentication, Role role) {
        UserAccount user = require(authentication);
        if (user.getRole() != role) throw new ForbiddenOperationException("This operation requires role " + role);
        return user;
    }
}
