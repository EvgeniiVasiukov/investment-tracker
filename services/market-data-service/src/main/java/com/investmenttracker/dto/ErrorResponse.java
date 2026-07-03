package com.investmenttracker.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        LocalDateTime timeStamp
) {
}
