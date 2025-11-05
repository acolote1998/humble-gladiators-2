package com.github.acolote1998.humble_gladiators_2.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;

/**
 * Test configuration that provides a mock JWT decoder that doesn't validate tokens.
 * This allows Spring Security Test's jwt() method to work without requiring a real OAuth2 issuer.
 * 
 * Note: Spring Security Test framework actually bypasses this decoder when using jwt() method,
 * but we need to provide a bean to satisfy the SecurityConfig dependency.
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        // Return a decoder that creates a basic JWT without validation
        // In practice, Spring Security Test's jwt() method will bypass this
        return token -> Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "test-user")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }
}

