package com.investmenttracker.apigateway.exception;

import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;


@Component
@Order(-2)
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {
    private final JsonMapper jsonMapper;

    public GlobalGatewayExceptionHandler(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof ResponseStatusException responseStatusException) {
            HttpStatusCode statusCode = responseStatusException.getStatusCode();
            if (statusCode.value() == HttpStatus.NOT_FOUND.value()) {
                return writeErrorResponse(exchange, HttpStatus.NOT_FOUND, GatewayErrorCode.ROUTE_NOT_FOUND, "Route not found");
            }
        }
        return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, GatewayErrorCode.INTERAL_GATEWAY_ERROR, "Internal gateway error");
    }
    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, GatewayErrorCode errorCode, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                errorCode,
                message
        );
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

