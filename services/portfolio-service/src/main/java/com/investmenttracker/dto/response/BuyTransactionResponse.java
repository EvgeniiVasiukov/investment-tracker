package com.investmenttracker.dto.response;

import com.investmenttracker.entity.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record BuyTransactionResponse(
        @Schema(
                description = "ID of the created transaction",
                example = "200"
        )
        Long transactionId,

        @Schema(
                description = "ID of the created or updated position",
                example = "100"
        )
        Long positionId,

        @Schema(
                description = "Ticker symbol",
                example = "NVDA"
        )
        String ticker,

        @Schema(
                description = "Total quantity in the position after the transaction",
                example = "15.000000"
        )
        BigDecimal quantity,

        @Schema(
                description = "Weighted average purchase price after the transaction",
                example = "125.500000"
        )
        BigDecimal averagePrice,

        @Schema(
                description = "Position currency",
                example = "USD"
        )
        Currency currency
) {
}
