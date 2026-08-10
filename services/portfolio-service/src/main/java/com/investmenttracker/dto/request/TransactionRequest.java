package com.investmenttracker.dto.request;

import com.investmenttracker.entity.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionRequest {
    String ticker();
    BigDecimal quantity();
    BigDecimal price();
    Currency currency();
    BigDecimal fees();
    BigDecimal tax();
    LocalDateTime transactionDate();
}
