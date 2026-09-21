package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.dto.AuthDtos;
import com.bbrz.sebastian.JobSwiperBackend.enums.Role;
import com.bbrz.sebastian.JobSwiperBackend.exception.ConflictException;
import com.bbrz.sebastian.JobSwiperBackend.exception.ForbiddenOperationException;
import com.bbrz.sebastian.JobSwiperBackend.model.UserAccount;
import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * Provides user registration, login and user loading for authentication.
 *
 * <p>Also creates JWT responses for successfully authenticated users.</p>
 */
@Service
public class AuthService implements UserDetailsService {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Creates the authentication service.
     *
     * @param users user repository
     * @param passwordEncoder encoder for passwords
     * @param jwtService service for creating JWTs
     */
    public AuthService(
            UserAccountRepository users,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user account.
     *
     * @param request registration data
     * @return authentication response with JWT
     */
    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        String username = request.username().trim().toLowerCase(Locale.ROOT);
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (request.role() == Role.ADMIN) {
            throw new ForbiddenOperationException(
                    "Admin accounts cannot self-register"
            );
        }

        if (users.existsByUsernameIgnoreCase(username)) {
            throw new ConflictException("Username is already in use");
        }

        if (users.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email is already in use");
        }

        UserAccount user = users.save(
                new UserAccount(
                        username,
                        email,
                        passwordEncoder.encode(request.password()),
                        request.role()
                )
        );

        return tokenResponse(user);
    }

    /**
     * Authenticates a user with username and password.
     *
     * @param request login data
     * @return authentication response with JWT
     */
    @Transactional(readOnly = true)
    public AuthDtos.AuthResponse authenticate(AuthDtos.LoginRequest request) {
        UserAccount user = users.findByUsernameIgnoreCase(
                        request.username().trim()
                )
                .filter(UserAccount::isActive)
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return tokenResponse(user);
    }

    /**
     * Creates a JWT response for a user.
     *
     * @param user authenticated user
     * @return authentication response
     */
    private AuthDtos.AuthResponse tokenResponse(UserAccount user) {
        String token = jwtService.generateToken(user);

        return AuthDtos.AuthResponse.of(
                token,
                jwtService.expirationFromNow(),
                user
        );
    }

    /**
     * Loads a user by username for Spring Security.
     *
     * @param username username to search for
     * @return matching user details
     * @throws UsernameNotFoundException if the user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return users.findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }
}
