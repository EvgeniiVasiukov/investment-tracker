package com.investmenttracker.analyticsservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

public record PriceDto(
        String ticker,
        BigDecimal currentPrice,
        Currency currency,
        String provider,
        Instant updatedAt
) {
}
