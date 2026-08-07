package com.investmenttracker.dto.request;

import com.investmenttracker.entity.Currency;

import java.math.BigDecimal;

public record PositionFilter (
        String ticker,
        Currency currency,
        BigDecimal minAveragePrice,
        BigDecimal maxAveragePrice,
        Long userId
) {
}
