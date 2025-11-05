package com.github.acolote1998.humble_gladiators_2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired(required = false)
    private JwtDecoder jwtDecoder;
    
    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        http
                .csrf((crsf -> crsf.disable()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/game/**").authenticated()
                        .requestMatchers("/api/campaign").authenticated()
                        .requestMatchers("/api/campaign/**").authenticated()
                        .requestMatchers("/api/campaign/*/items-booster").authenticated()
                        .requestMatchers("/api/campaign/*/character-instances/hero").authenticated()

                )
                .cors(withDefaults());
        
        // Only configure OAuth2 Resource Server if JWT decoder is available
        if (jwtDecoder != null) {
            http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)));
        }
        
        return http.build();
    }
}
