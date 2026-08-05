package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvestedAmountCalculator {
    public BigDecimal calculate(List<PortfolioSnapshotPosition> positions) {
        if (positions == null || positions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return positions.stream()
                .map(this::calculateInvestedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private BigDecimal calculateInvestedAmount(PortfolioSnapshotPosition portfolioSnapshotPosition) {
        return portfolioSnapshotPosition.averagePrice().multiply(portfolioSnapshotPosition.quantity());
    }
}
