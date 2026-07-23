package com.investmenttracker.apigateway.exception;

import java.time.Instant;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        GatewayErrorCode errorCode,
        String message

) {
}
