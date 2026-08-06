package com.investmenttracker.analyticsservice.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record DashboardResponse(
        BigDecimal portfolioValue,
        BigDecimal investedAmount,
        BigDecimal profitLoss,
        BigDecimal remainingCredit,
        BigDecimal netWorth,
        Currency currency
) {
}
