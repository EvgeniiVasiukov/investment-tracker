package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.dto.PortfolioPositionDto;
import com.investmenttracker.analyticsservice.dto.PriceDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PortfolioValueCalculator {
    public BigDecimal calculate(List<PortfolioPositionDto> positions,
                                Map<String, PriceDto> priceByTicker) {
        return new BigDecimal(0);
    }
}
