package com.bbrz.sebastian.JobSwiperBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the backend API.
 *
 * <p>The allowed origins are loaded from {@link CorsProperties}.
 * CORS rules are applied to all endpoints under {@code /api/**}.</p>
 */
@Configuration
public class CorsConfig {

    private final CorsProperties properties;

    /**
     * Creates the CORS configuration using the configured allowed origins.
     *
     * @param properties CORS settings from the application configuration
     */
    public CorsConfig(CorsProperties properties) {
        this.properties = properties;
    }

    /**
     * Defines the CORS rules for API requests.
     *
     * <p>Allows common HTTP methods and the Authorization and Content-Type headers.</p>
     *
     * @return the configured CORS configuration source
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(properties.getAllowedOrigins());
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type")
        );

        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
