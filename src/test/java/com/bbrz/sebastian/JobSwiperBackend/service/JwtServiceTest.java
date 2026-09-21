package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.config.JwtConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "jobswiper-unit-test-secret-at-least-thirty-two-bytes";

    @Test
    @DisplayName("Testfall 11 - JWT-Generierung und Validierung")
    void generatedTokenIsValidAndExpiredTokenIsInvalid() {
        var user = User.withUsername("employee").password("unused").roles("ARBEITNEHMER").build();

        JwtService validTokens = new JwtService(configWithExpiration(60_000));
        String validToken = validTokens.generateToken(user);
        assertThat(validTokens.isTokenValid(validToken, user)).isTrue();

        JwtService expiredTokens = new JwtService(configWithExpiration(-1_000));
        String expiredToken = expiredTokens.generateToken(user);
        assertThat(expiredTokens.isTokenValid(expiredToken, user)).isFalse();
    }

    private JwtConfig configWithExpiration(long expiration) {
        JwtConfig config = new JwtConfig();
        config.setSecret(SECRET);
        config.setExpiration(expiration);
        return config;
    }
}
