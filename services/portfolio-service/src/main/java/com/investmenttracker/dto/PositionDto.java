package com.investmenttracker.dto;

import com.investmenttracker.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PositionDto (
    Long id,
    Long userId,
    String ticker,
    BigDecimal quantity,
    BigDecimal averagePrice,
    Currency currency,
    LocalDateTime createdAt
)
{}
