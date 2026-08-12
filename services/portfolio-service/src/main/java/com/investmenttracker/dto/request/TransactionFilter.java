package com.investmenttracker.dto.request;

import com.investmenttracker.entity.TransactionType;

import java.time.LocalDateTime;

public record TransactionFilter(
        String ticker,
        TransactionType transactionType,
        LocalDateTime dateFrom,
        LocalDateTime dateTo
) {
}
