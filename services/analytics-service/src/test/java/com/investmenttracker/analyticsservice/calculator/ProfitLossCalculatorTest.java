package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class ProfitLossCalculatorTest {
    private final ProfitLossCalculator calculator = new ProfitLossCalculator();
    @Test
    void shouldCalculateProfit() {
        List<PortfolioSnapshotPosition> positions = List.of(
                new PortfolioSnapshotPosition(
                        "NVDA",
                        new BigDecimal(2),
                        new BigDecimal("100"),
                        new BigDecimal("50"),
                        Currency.getInstance("USD"))
                , new PortfolioSnapshotPosition(
                        "MSFT",
                        new BigDecimal(10),
                        new BigDecimal(50),
                        new BigDecimal("25"),
                        Currency.getInstance("USD")
                ));

        BigDecimal result = calculator.calculate(positions);

        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(350)));
    }
    @Test
    void shouldCalculateLoss() {
        List<PortfolioSnapshotPosition> positions = List.of(
                new PortfolioSnapshotPosition(
                        "NVDA",
                        new BigDecimal(2),
                        new BigDecimal("50"),
                        new BigDecimal("100"),
                        Currency.getInstance("USD"))
                , new PortfolioSnapshotPosition(
                        "MSFT",
                        new BigDecimal(10),
                        new BigDecimal(50),
                        new BigDecimal("250"),
                        Currency.getInstance("USD")
                ));

        BigDecimal result = calculator.calculate(positions);

        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(-2100)));
    }
    @Test
    void shouldCalculateZeroLossAndZeroProfit() {
        List<PortfolioSnapshotPosition> positions = List.of(
                new PortfolioSnapshotPosition(
                        "NVDA",
                        new BigDecimal(2),
                        new BigDecimal("10"),
                        new BigDecimal("10"),
                        Currency.getInstance("USD"))
                , new PortfolioSnapshotPosition(
                        "MSFT",
                        new BigDecimal(10),
                        new BigDecimal(10),
                        new BigDecimal(10),
                        Currency.getInstance("USD")
                ));

        BigDecimal result = calculator.calculate(positions);

        Assertions.assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
    @Test
    void shouldReturnZeroWhenNoPositions() {
        List<PortfolioSnapshotPosition> positions = List.of();
        BigDecimal result = calculator.calculate(positions);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
    @Test
    void shouldReturnZeroForNullPortfolio() {
        var result = calculator.calculate(null);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
}
