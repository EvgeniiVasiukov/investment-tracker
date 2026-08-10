package com.investmenttracker.dto.response;

import com.investmenttracker.entity.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record SellTransactionResponse(
        @Schema(
                description = "ID of the created transaction",
                example = "201"
        )
        Long transactionId,

        @Schema(
                description = "ID of the affected position",
                example = "100"
        )
        Long positionId,

        @Schema(
                description = "Ticker symbol",
                example = "NVDA"
        )
        String ticker,

        @Schema(
                description = "Remaining quantity in the position after the transaction",
                example = "6.000000"
        )
        BigDecimal quantity,

        @Schema(
                description = "Average purchase price of the position",
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
