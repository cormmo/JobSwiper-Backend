package com.bbrz.sebastian.JobSwiperBackend.config;

import com.bbrz.sebastian.JobSwiperBackend.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configures Spring Security for the application.
 *
 * <p>Defines protected endpoints, JWT authentication and stateless sessions.</p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final GlobalExceptionHandler exceptionHandler;

    /**
     * Creates the security configuration.
     *
     * @param jwtAuthFilter filter used for JWT authentication
     * @param exceptionHandler centralized API exception handler
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, GlobalExceptionHandler exceptionHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Defines the security rules for HTTP requests.
     *
     * @param http Spring Security HTTP configuration
     * @return the configured security filter chain
     * @throws Exception if the configuration fails
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disabled because cookie based authentication is not in use
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/openapi.yaml",
                                "/h2-console",
                                "/h2-console/**"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint(exceptionHandler)
                        .accessDeniedHandler(exceptionHandler)
                )

                /*
                 * Spring Security usually blocks frames, this allows frames only from same origin
                 * to access h2 console
                 */

                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin()))
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * Provides the password encoder used for user passwords.
     *
     * @return BCrypt password encoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
