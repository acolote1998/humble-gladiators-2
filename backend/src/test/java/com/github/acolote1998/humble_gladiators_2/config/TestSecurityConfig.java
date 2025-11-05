package com.github.acolote1998.humble_gladiators_2.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;

/**
 * Test configuration that provides a mock JWT decoder that doesn't validate tokens.
 * This allows Spring Security Test's jwt() method to work without requiring a real OAuth2 issuer.
 * 
 * This configuration is only in the test source folder, so it won't be loaded in production.
 * Import this class in @WebMvcTest tests that need OAuth2 support:
 * @Import(TestSecurityConfig.class)
 * 
 * The @Primary ensures this decoder is used instead of any auto-configured one.
 */
@TestConfiguration
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

