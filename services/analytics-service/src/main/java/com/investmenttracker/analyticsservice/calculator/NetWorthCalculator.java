package com.investmenttracker.analyticsservice.calculator;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NetWorthCalculator {
    public BigDecimal calculate(BigDecimal portfolioValue, BigDecimal remainingCreditBalance) {
        if (portfolioValue == null) {
            portfolioValue = BigDecimal.ZERO;
        }
        if (remainingCreditBalance == null) {
            remainingCreditBalance = BigDecimal.ZERO;
        }
        return portfolioValue.subtract(remainingCreditBalance);
    }
}
