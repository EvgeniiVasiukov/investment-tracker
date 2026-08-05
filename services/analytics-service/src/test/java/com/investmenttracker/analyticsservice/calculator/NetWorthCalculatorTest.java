package com.investmenttracker.analyticsservice.calculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class NetWorthCalculatorTest {
    private final NetWorthCalculator calculator = new NetWorthCalculator();

    @Test
    void shouldCalculateNetWorthWithoutCredit() {
        BigDecimal result = calculator.calculate(BigDecimal.valueOf(100.00), BigDecimal.ZERO);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(100.00)));
    }
    @Test
    void shouldCalculateNetWorthWithCredit() {
        BigDecimal result = calculator.calculate(BigDecimal.valueOf(10000.00), BigDecimal.valueOf(3000.00));
        assertEquals(0, result.compareTo(BigDecimal.valueOf(7000.00)));
    }
    @Test
    void shouldReturnNegativeNetWorth() {
        BigDecimal result = calculator.calculate(BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00));
        assertEquals(0, result.compareTo(BigDecimal.valueOf(-100.00)));
    }
    @Test
    void shouldTreatNullPortfolioAsZero() {
        BigDecimal result = calculator.calculate(null, BigDecimal.valueOf(100.00));
        assertEquals(0, result.compareTo(BigDecimal.valueOf(-100.00)));
    }
    @Test
    void shouldTreatNullCreditBalanceAsZero() {
        BigDecimal result = calculator.calculate(BigDecimal.valueOf(100.00), null);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(100.00)));
    }
    @Test
    void shouldReturnZeroWhenBothValueIsNull() {
        BigDecimal result = calculator.calculate(null, null);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
}
