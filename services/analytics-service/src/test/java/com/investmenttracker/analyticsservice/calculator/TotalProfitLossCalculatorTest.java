package com.investmenttracker.analyticsservice.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class TotalProfitLossCalculatorTest {

    private final TotalProfitLossCalculator calculator =
            new TotalProfitLossCalculator();

    @Test
    void shouldCalculateTotalProfit() {
        BigDecimal result = calculator.calculate(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(50)
        );

        Assertions.assertEquals(
                0,
                result.compareTo(BigDecimal.valueOf(150))
        );
    }

    @Test
    void shouldCalculateTotalProfitLossWithNegativeRealizedProfitLoss() {
        BigDecimal result = calculator.calculate(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(-30)
        );

        Assertions.assertEquals(
                0,
                result.compareTo(BigDecimal.valueOf(70))
        );
    }

    @Test
    void shouldCalculateTotalProfitLossWithNegativeUnrealizedProfitLoss() {
        BigDecimal result = calculator.calculate(
                BigDecimal.valueOf(-100),
                BigDecimal.valueOf(30)
        );

        Assertions.assertEquals(
                0,
                result.compareTo(BigDecimal.valueOf(-70))
        );
    }

    @Test
    void shouldHandleZeroRealizedProfitLoss() {
        BigDecimal result = calculator.calculate(
                BigDecimal.valueOf(100),
                BigDecimal.ZERO
        );

        Assertions.assertEquals(
                0,
                result.compareTo(BigDecimal.valueOf(100))
        );
    }

    @Test
    void shouldHandleZeroUnrealizedProfitLoss() {
        BigDecimal result = calculator.calculate(
                BigDecimal.ZERO,
                BigDecimal.valueOf(50)
        );

        Assertions.assertEquals(
                0,
                result.compareTo(BigDecimal.valueOf(50))
        );
    }
}