package com.investmenttracker.analyticsservice.calculator;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TotalProfitLossCalculator {
    public BigDecimal calculate(BigDecimal unrealizedProfitLoss, BigDecimal realizedProfitLoss) {
        return unrealizedProfitLoss.add(realizedProfitLoss);
    }
}
