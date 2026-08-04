package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class PortfolioValueCalculatorTest {
    private final PortfolioValueCalculator calculator = new PortfolioValueCalculator();
    @Test
    void shouldCalculateTotalPortfolioValue() {
        List<PortfolioSnapshotPosition> positions = List.of(
                new PortfolioSnapshotPosition(
                        "NVDA",
                        new BigDecimal(2),
                        new BigDecimal("100"),
                        Currency.getInstance("USD"))
        , new PortfolioSnapshotPosition(
                "MSFT",
                new BigDecimal(10),
                new BigDecimal(50),
                Currency.getInstance("USD")
        ));

        BigDecimal result = calculator.calculate(positions);

        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(700)));
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
