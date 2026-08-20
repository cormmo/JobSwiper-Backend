package com.bbrz.sebastian.JobSwiperBackend.dto;

import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import jakarta.validation.constraints.*;

import java.time.Instant;

public final class AuthDtos {
    private AuthDtos() {}

    public record RegisterRequest(
            @NotBlank @Size(min = 3, max = 32) @Pattern(regexp = "^[A-Za-z0-9._-]+$") String username,
            @NotBlank @Email @Size(max = 254) String email,
            @NotBlank @Size(min = 8, max = 72) String password,
            @NotNull Role role) {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password) {}

    public record UserResponse(Long id, String username, String email, Role role, boolean active, Instant createdAt) {
        public static UserResponse from(UserAccount user) {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                    user.isActive(), user.getCreatedAt());
        }
    }

    public record AuthResponse(String token, String tokenType, Instant expiresAt, UserResponse user) {
        public static AuthResponse of(String token, Instant expiresAt, UserAccount user) {
            return new AuthResponse(token, "Bearer", expiresAt, UserResponse.from(user));
        }
    }
}
