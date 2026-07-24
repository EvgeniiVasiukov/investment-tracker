package com.investmenttracker.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var method = request.getMethod();
        var path = request.getPath().value();
        var startTime = System.nanoTime();
        log.info("Incoming request: method={}, path={}", method, path);
        return chain.filter(exchange)
                .doFinally(signalType -> {
                    var durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - startTime;
                    var statusCode = exchange.getResponse().getStatusCode();
                    log.info(
                            "Completed request: method={}, path={}, status={}, durationMs={}",
                            method, path, statusCode != null ? statusCode.value() : "UNKNOWN", durationMs);
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
