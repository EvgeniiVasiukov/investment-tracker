package com.investmenttracker.dto.request;

import com.investmenttracker.entity.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BuyTransactionRequest(
        @Schema(example = "NVDA")
        @NotBlank
        String ticker,
        @Schema(example = "10.000000")
        @NotNull @Positive
        BigDecimal quantity,
        @Schema(example = "125.500")
        @NotNull @Positive
        BigDecimal price,
        @Schema(example = "USD")
        @NotNull
        Currency currency,
        @Schema(example = "1.000")
        @NotNull @PositiveOrZero
        BigDecimal fees,
        @Schema(example = "0.00")
        @NotNull @PositiveOrZero
        BigDecimal tax,
        @Schema(example = "2026-08-10T12:00:00")
        @NotNull
        LocalDateTime transactionDate
) implements TransactionRequest {
}
