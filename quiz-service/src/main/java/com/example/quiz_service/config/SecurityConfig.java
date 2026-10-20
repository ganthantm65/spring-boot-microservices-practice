
package com.example.quiz_service.config;

import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final String SECRET_KEY =
            "quizsystemmicroservicejwtsecretkey2026secure123456789";

    private static final Logger logger =
            LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = SECRET_KEY.getBytes(
                java.nio.charset.StandardCharsets.UTF_8
        );

        SecretKey secretKeyEntry =
                new SecretKeySpec(keyBytes, "HmacSHA256");

        JwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(secretKeyEntry)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        return token -> {
            try {
                return decoder.decode(token);
            } catch (JwtException e) {
                logger.error("JWT validation failed: {}", e.getMessage(), e);
                throw e;
            }
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter);

        return authenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(configurationSource()))
            .authorizeHttpRequests(auth ->
                auth.anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth ->
                oauth.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                            jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsCon = new CorsConfiguration();

        corsCon.addAllowedOrigin("http://localhost:5173");
        corsCon.addAllowedHeader("*");
        corsCon.addAllowedMethod("GET");
        corsCon.addAllowedMethod("POST");
        corsCon.addAllowedMethod("PUT");
        corsCon.addAllowedMethod("DELETE");
        corsCon.addAllowedMethod("OPTIONS");
        corsCon.setAllowCredentials(true);
        corsCon.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource corsConfigurationSource =
                new UrlBasedCorsConfigurationSource();

        corsConfigurationSource.registerCorsConfiguration(
                "/**", corsCon);

        return corsConfigurationSource;
    }
}
