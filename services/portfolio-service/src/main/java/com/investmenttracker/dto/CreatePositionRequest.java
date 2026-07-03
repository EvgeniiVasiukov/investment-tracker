package com.investmenttracker.dto;

import com.investmenttracker.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePositionRequest(
        @NotNull
        @NotBlank
        String ticker,
        @Positive
        @NotNull
        BigDecimal quantity,
        @NotNull
        Currency currency,
        @Positive
        @NotNull
        BigDecimal averagePrice
) {
}
