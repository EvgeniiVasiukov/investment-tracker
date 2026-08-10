package com.investmenttracker.dto.response;

import com.investmenttracker.entity.Currency;

import java.math.BigDecimal;

public record SellTransactionResponse(
        Long transactionId,
        Long positionId,
        String ticker,
        BigDecimal quantity,
        BigDecimal averagePrice,
        Currency currency
) {
}
