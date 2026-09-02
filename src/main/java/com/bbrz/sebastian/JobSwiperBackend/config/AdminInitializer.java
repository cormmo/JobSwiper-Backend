package com.bbrz.sebastian.JobSwiperBackend.config;

import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * Creates an administrator account when the application starts.
 *
 * <p>The admin credentials are loaded from the application configuration.
 * The account is only created if all values are provided and no user with
 * the same username or email already exists.</p>
 */
@Component
public class AdminInitializer implements ApplicationRunner {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String email;
    private final String password;

    /**
     * Creates the admin initializer with the required dependencies and
     * configured administrator credentials.
     *
     * @param users repository for user accounts
     * @param passwordEncoder encoder for hashing passwords
     * @param username administrator username
     * @param email administrator email
     * @param password administrator password
     */
    public AdminInitializer(UserAccountRepository users, PasswordEncoder passwordEncoder,
                            @Value("${app.admin.username:}") String username,
                            @Value("${app.admin.email:}") String email,
                            @Value("${app.admin.password:}") String password) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Creates the administrator account on application startup.
     *
     * <p>The password must contain at least 12 characters.
     * Existing usernames or email addresses are not overwritten.</p>
     *
     * @param args application startup arguments
     * @throws IllegalStateException if the password is shorter than 12 characters
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) return;

        if (password.length() < 12) {
            throw new IllegalStateException(
                    "ADMIN_PASSWORD must contain at least 12 characters"
            );
        }

        if (users.existsByUsernameIgnoreCase(username)
                || users.existsByEmailIgnoreCase(email)) return;

        users.save(new UserAccount(
                username.trim().toLowerCase(Locale.ROOT),
                email.trim().toLowerCase(Locale.ROOT),
                passwordEncoder.encode(password),
                Role.ADMIN
        ));
    }
}
