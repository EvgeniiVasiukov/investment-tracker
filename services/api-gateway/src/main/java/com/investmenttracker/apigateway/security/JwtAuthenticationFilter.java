package com.investmenttracker.apigateway.security;

import com.investmenttracker.apigateway.exception.ApiErrorResponse;
import com.investmenttracker.apigateway.exception.GatewayErrorCode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtTokenValidator jwtTokenValidator;
    private JsonMapper jsonMapper;

    public JwtAuthenticationFilter(JwtTokenValidator jwtTokenValidator, JsonMapper jsonMapper) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isPublicEndPoint(request)) {
            return chain.filter(exchange);
        }
        HttpHeaders headers = request.getHeaders();
        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if ((authorizationHeader == null) || !authorizationHeader.startsWith("Bearer ")) {
            return sendUnauthorizedStatus(exchange);
        }
        String token = authorizationHeader.substring(7);
        if (!jwtTokenValidator.isTokenValid(token)) {
            return sendUnauthorizedStatus(exchange);
        }
        return chain.filter(exchange);

    }
    private boolean isPublicEndPoint(ServerHttpRequest request) {
        HttpMethod httpMethod = request.getMethod();
        String path = request.getPath().value();
        for (PublicEndpoint publicEndpoint : PublicEndpoint.values()) {
            if (Objects.equals(httpMethod, publicEndpoint.getHttpMethod())
                    && Objects.equals(path, publicEndpoint.getPath())) {
                return true;
            }
        }
        return false;
    }
    private Mono<Void> sendUnauthorizedStatus(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                GatewayErrorCode.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.getReasonPhrase()
        );
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte [] responseBody;
        try {
            responseBody = jsonMapper.writeValueAsBytes(apiErrorResponse);

        } catch (JacksonException e) {
            return response.setComplete();
        }
        DataBuffer buffer = response.bufferFactory().wrap(responseBody);
        return response.writeWith(Mono.just(buffer));
    }
}
