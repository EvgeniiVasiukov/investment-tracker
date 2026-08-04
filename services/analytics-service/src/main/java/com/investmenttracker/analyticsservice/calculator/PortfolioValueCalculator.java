package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.dto.PortfolioPositionDto;
import com.investmenttracker.analyticsservice.dto.PriceDto;
import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioValueCalculator {
    public BigDecimal calculate(List<PortfolioSnapshotPosition> positions) {
        if (positions == null) {
            return BigDecimal.ZERO;
        }
        return positions.stream()
                .map(this::calculatePositionValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
    private BigDecimal calculatePositionValue(PortfolioSnapshotPosition portfolioSnapshotPosition) {
        return portfolioSnapshotPosition.currentPrice().multiply(portfolioSnapshotPosition.quantity());
    }
}
