package com.investmenttracker.dto.response;

import com.investmenttracker.entity.Currency;
import com.investmenttracker.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        TransactionType transactionType,
        String ticker,
        BigDecimal quantity,
        BigDecimal price,
        Currency currency,
        BigDecimal fees,
        BigDecimal tax,
        BigDecimal realizedProfitLoss,
        LocalDateTime transactionDate
) {
}
