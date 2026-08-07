package com.investmenttracker.dto.request;

import com.investmenttracker.entity.Currency;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdatePostionRequest(
        String ticker,
        @Positive
        BigDecimal quantity,
        Currency currency,
        @Positive
        BigDecimal averagePrice
) {
}
