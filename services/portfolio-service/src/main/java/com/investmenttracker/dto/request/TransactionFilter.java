package com.investmenttracker.dto.request;

import com.investmenttracker.entity.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TransactionFilter(
        String ticker,
        TransactionType transactionType,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dateTo
) {
}
