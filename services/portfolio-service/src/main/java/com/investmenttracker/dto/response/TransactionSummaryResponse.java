package com.investmenttracker.dto.response;

import java.math.BigDecimal;

public record TransactionSummaryResponse(
        BigDecimal realizedProfitLoss
) {
}
