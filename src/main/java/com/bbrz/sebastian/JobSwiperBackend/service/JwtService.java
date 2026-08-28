package com.bbrz.sebastian.JobSwiperBackend.service;

import com.bbrz.sebastian.JobSwiperBackend.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Creates and validates HMAC-SHA-256 JSON Web Tokens.
 *
 * <p>The authenticated user's username is stored as the token subject. The signing secret and
 * token lifetime are provided by {@link JwtConfig}.</p>
 */
@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    /**
     * @param jwtConfig the signing and expiration configuration
     */
    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Generates a signed JWT whose subject is the user's username.
     *
     * @param user the user for whom to generate the token
     * @return the compact, serialized JWT
     */
    public String generateToken(UserDetails user) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtConfig.getExpiration())))
                .signWith(signingKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * @return the expiration time for a token issued now
     */
    public Instant expirationFromNow() {
        return Instant.now().plusMillis(jwtConfig.getExpiration());
    }

    /**
     * Extracts the username from a verified token.
     *
     * @param token the JWT to parse
     * @return the username stored in the subject claim
     * @throws io.jsonwebtoken.JwtException if the token cannot be parsed or verified
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Checks whether a verified token belongs to the given enabled user and has not expired.
     * Username comparison is case-insensitive.
     *
     * @param token the JWT to validate
     * @param user the expected token owner
     * @return {@code true} if the token is valid for the user; otherwise {@code false}
     * @throws io.jsonwebtoken.JwtException if the token cannot be parsed or verified
     */
    public boolean isTokenValid(String token, UserDetails user) {
        Claims claims = getClaims(token);

        return user.isEnabled()
                && user.getUsername().equalsIgnoreCase(claims.getSubject())
                && claims.getExpiration().after(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(
                jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }
}
