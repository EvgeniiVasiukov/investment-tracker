package com.investmenttracker.dto.request;

import com.investmenttracker.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BuyTransactionRequest(
        @NotBlank
        String ticker,
        @NotNull @Positive
        BigDecimal quantity,
        @NotNull @Positive
        BigDecimal price,
        @NotNull
        Currency currency,
        @NotNull @PositiveOrZero
        BigDecimal fees,
        @NotNull @PositiveOrZero
        BigDecimal tax,
        @NotNull
        LocalDateTime transactionDate
) {
}
