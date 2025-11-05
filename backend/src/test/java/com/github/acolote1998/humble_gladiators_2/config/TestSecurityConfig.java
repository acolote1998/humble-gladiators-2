package com.github.acolote1998.humble_gladiators_2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;

/**
 * Test configuration that provides a mock JWT decoder that doesn't validate tokens.
 * This allows Spring Security Test's jwt() method to work without requiring a real OAuth2 issuer.
 * 
 * This configuration is only in the test source folder, so it won't be loaded in production.
 * The @Order(-1) and @Primary ensure this decoder is used instead of any auto-configured one.
 * By always providing a decoder, we prevent OAuth2ResourceServerAutoConfiguration from trying
 * to create one from issuer-uri (which would fail in CI without CLERK_ISSUER_URL).
 */
@Configuration
@Order(-1)
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

