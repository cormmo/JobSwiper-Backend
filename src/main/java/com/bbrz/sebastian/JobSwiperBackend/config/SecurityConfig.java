package com.bbrz.sebastian.JobSwiperBackend.config;

import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * Creates the security configuration.
     *
     * @param jwtAuthFilter filter used for JWT authentication
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
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
                        .authenticationEntryPoint((request, response, exception) ->
                                writeSecurityError(
                                        response,
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        "Unauthorized",
                                        "Authentication is required"
                                ))
                        .accessDeniedHandler((request, response, exception) ->
                                writeSecurityError(
                                        response,
                                        HttpServletResponse.SC_FORBIDDEN,
                                        "Forbidden",
                                        "Access is denied"
                                ))
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

    /**
     * Writes a JSON response for authentication and authorization errors.
     *
     * @param response HTTP response
     * @param status HTTP status code
     * @param title error title
     * @param detail error description
     * @throws java.io.IOException if the response cannot be written
     */
    private static void writeSecurityError(
            HttpServletResponse response,
            int status,
            String title,
            String detail
    ) throws java.io.IOException {

        response.setStatus(status);
        response.setContentType("application/problem+json");

        response.getWriter().write(
                "{\"type\":\"about:blank\",\"title\":\"" + title
                        + "\",\"status\":" + status
                        + ",\"detail\":\"" + detail + "\"}"
        );
    }
}
