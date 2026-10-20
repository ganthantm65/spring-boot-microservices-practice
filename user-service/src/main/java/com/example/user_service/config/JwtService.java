
package com.example.user_service.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private final String SECRET_KEY =
            "quizsystemmicroservicejwtsecretkey2026secure123456789";

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String generateToken(UserDetails userDetails,
                               Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + 1000L * 60 * 60
                ))
                .signWith(
                    Keys.hmacShaKeyFor(
                        SECRET_KEY.getBytes(
                            java.nio.charset.StandardCharsets.UTF_8
                        )
                    ),
                    Jwts.SIG.HS256
                )
                .compact();
    }

    public boolean isTokenValid(UserDetails userDetails, String token) {
        Claims claims = getClaims(token);

        return claims.getSubject().equals(userDetails.getUsername())
                && claims.getExpiration().after(new Date());
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(
                        Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes(
                                        java.nio.charset.StandardCharsets.UTF_8
                                )
                        )
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
