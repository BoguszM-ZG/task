package com.tcode.moviebase.Security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;
import java.util.*;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/favourites/**").authenticated()
                        .requestMatchers("/to-watch/**") .authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            Map<String, Object> headers = new HashMap<>();
            headers.put("alg", "RS256");

            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", "bd237d7d-b52f-41b8-a83b-02532a90d792");
            Map<String, Object> realmAccess = new HashMap<>();
            realmAccess.put("roles", List.of("client_admin"));
            claims.put("realm_access", realmAccess);

            return new Jwt(token, Instant.now(), Instant.now().plusSeconds(3600), headers, claims);
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                return Collections.emptyList();
            }
            List<String> roles = (List<String>) realmAccess.get("roles");
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return authorities;
        });
        return converter;
    }


}