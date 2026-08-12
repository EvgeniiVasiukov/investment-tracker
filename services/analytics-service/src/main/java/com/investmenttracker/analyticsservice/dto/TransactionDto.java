package com.investmenttracker.analyticsservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public record TransactionDto(
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
