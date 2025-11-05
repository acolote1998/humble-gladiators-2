package com.github.acolote1998.humble_gladiators_2.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;

/**
 * Test configuration that provides a mock JWT decoder that doesn't validate tokens.
 * This allows Spring Security Test's jwt() method to work without requiring a real OAuth2 issuer.
 * 
 * This configuration is only in the test source folder, so it won't be loaded in production.
 * It provides a JWT decoder only if one doesn't already exist (from OAuth2 auto-configuration).
 */
@Configuration
@ConditionalOnMissingBean(JwtDecoder.class)
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        // Return a decoder that creates a basic JWT without validation
        // Spring Security Test framework will use this when jwt() method is called
        return token -> Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "test-user")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }
}

