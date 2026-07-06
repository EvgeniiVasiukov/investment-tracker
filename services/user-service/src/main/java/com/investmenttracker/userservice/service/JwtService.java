package com.investmenttracker.userservice.service;


import com.investmenttracker.userservice.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    long expiration;

    public String generateToken(User user) {
        JwtBuilder builder = Jwts.builder();
        builder.subject(user.getId().toString());
        builder.claim("email", user.getEmail());
        builder.claim("role", user.getRole().name());
        builder.claim("status", user.getStatus().name());
        builder.issuedAt(new Date());
        builder.expiration(new Date(System.currentTimeMillis() + expiration));
        builder.signWith(getSigningKey());
        return builder.compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public Long extractUserId(String token) {
        return Long.valueOf(extractAllClaims(token).getSubject());
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        }   catch (JwtException e) {
            return false;
        }
    }
}

