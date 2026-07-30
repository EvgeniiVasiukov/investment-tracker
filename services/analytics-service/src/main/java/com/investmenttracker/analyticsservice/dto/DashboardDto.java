package com.investmenttracker.analyticsservice.dto;

import java.time.Instant;

public record DashboardDto(
        MoneyDto totalPortfolioValue,
        MoneyDto totalInvestedAmount,
        MoneyDto unrealizedProfitLoss,
        MoneyDto remainingCreditBalance,
        MoneyDto netWorth,
        Instant calculatedAt
) {
}
