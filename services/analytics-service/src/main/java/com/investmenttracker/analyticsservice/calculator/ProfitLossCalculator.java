package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProfitLossCalculator {
    public BigDecimal calculate(List<PortfolioSnapshotPosition> positions) {
        if (positions == null) {
            return BigDecimal.ZERO;
        }
        return positions.stream()
                .map(this::calculateProfitLoss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateProfitLoss(PortfolioSnapshotPosition position) {
        var positionCurrentValue = position.currentPrice().multiply(position.quantity());
        var positionInvestedAmount = position.averagePrice().multiply(position.quantity());
        return positionCurrentValue.subtract(positionInvestedAmount);
    }
}
