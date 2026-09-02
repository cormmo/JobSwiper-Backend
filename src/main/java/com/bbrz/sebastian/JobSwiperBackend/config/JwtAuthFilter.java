package com.bbrz.sebastian.JobSwiperBackend.config;

import com.bbrz.sebastian.JobSwiperBackend.repository.UserAccountRepository;
import com.bbrz.sebastian.JobSwiperBackend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that authenticates requests using a JWT from the Authorization header.
 *
 * <p>If a valid Bearer token is found, the corresponding user is loaded and
 * added to the Spring Security context.</p>
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserAccountRepository users;

    /**
     * Creates the JWT authentication filter.
     *
     * @param jwtService service used to read and validate JWTs
     * @param users repository used to load user accounts
     */
    public JwtAuthFilter(JwtService jwtService, UserAccountRepository users) {
        this.jwtService = jwtService;
        this.users = users;
    }

    /**
     * Checks incoming requests for a Bearer token and authenticates the user
     * if the token is valid.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param chain filter chain used to continue the request
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = header.substring(7);

            try {
                String username = jwtService.extractUsername(token);

                users.findByUsernameIgnoreCase(username)
                        .filter(user -> jwtService.isTokenValid(token, user))
                        .ifPresent(user -> {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            user,
                                            null,
                                            user.getAuthorities()
                                    );

                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            SecurityContextHolder.getContext()
                                    .setAuthentication(authentication);
                        });

            } catch (JwtException | IllegalArgumentException ignored) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}
