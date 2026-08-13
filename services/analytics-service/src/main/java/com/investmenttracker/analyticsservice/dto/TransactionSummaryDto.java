package com.investmenttracker.analyticsservice.dto;

import java.math.BigDecimal;

public record TransactionSummaryDto(
        BigDecimal realizedProfitLoss
) {
}
