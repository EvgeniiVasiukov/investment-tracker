package com.investmenttracker.analyticsservice.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        AnalyticsErrorCode code,
        String message
) {
}
