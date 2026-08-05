package com.investmenttracker.analyticsservice.model;

import java.math.BigDecimal;
import java.util.Currency;

public record PortfolioSnapshotPosition(
        String ticker,
        BigDecimal quantity,
        BigDecimal currentPrice,
        BigDecimal averagePrice,
        Currency currency
) {
}
