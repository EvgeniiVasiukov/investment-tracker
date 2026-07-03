package com.investmenttracker.dto;

import com.investmenttracker.model.Currency;
import com.investmenttracker.model.MarketDataProvider;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record PriceDto(
        String ticker,
        BigDecimal currentPrice,
        Currency currency,
        MarketDataProvider provider,
        Instant updatedAt
) {
}
