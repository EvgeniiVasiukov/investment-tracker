package com.investmenttracker.apigateway.security;

import lombok.Getter;
import org.springframework.http.HttpMethod;


public enum PublicEndpoint {
    LOGIN(HttpMethod.POST, "/auth/login"),
    REGISTER(HttpMethod.POST, "/auth/register"),
    HEALTH(HttpMethod.GET, "/actuator/health"),;

    @Getter
    private final HttpMethod httpMethod;
    @Getter
    private final String path;

    PublicEndpoint(HttpMethod method, String path) {
        this.httpMethod = method;
        this.path = path;
    }
}
