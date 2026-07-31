package com.investmenttracker.analyticsservice.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record PortfolioPositionDto(
    String ticker,
    BigDecimal quantity,
    BigDecimal averagePrice,
    Currency currency
) {
}
