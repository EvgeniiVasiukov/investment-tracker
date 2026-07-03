package com.investmenttracker.dto;

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
