package com.investmenttracker.apigateway.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenValidator {
    private final JwtProperties properties;

    public JwtTokenValidator(JwtProperties properties) {
        this.properties = properties;
    }

    private SecretKey getSecretKey() {
        return Keys.h
    }
}
