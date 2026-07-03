package com.investmenttracker.dto;

import com.investmenttracker.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdatePostionRequest(
        String ticker,
        @Positive
        BigDecimal quantity,
        Currency currency,
        @Positive
        BigDecimal averagePrice
) {
}
