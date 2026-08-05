package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.dto.CreditStatus;
import com.investmenttracker.analyticsservice.model.CreditSnapshot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class RemainingCreditCalculatorTest {
    private final RemainingCreditCalculator calculator = new RemainingCreditCalculator();
    @Test
    void shouldReturnRemainingBalanceForActiveCredit() {
        CreditSnapshot credit = new CreditSnapshot(BigDecimal.valueOf(10000), CreditStatus.ACTIVE);
        BigDecimal result = calculator.calculate(credit);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(10000)));
    }
    @Test
    void shouldReturnZeroForInactiveCredit() {
        CreditSnapshot credit = new CreditSnapshot(BigDecimal.valueOf(10000), CreditStatus.CLOSED);
        BigDecimal result = calculator.calculate(credit);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
    @Test
    void shouldReturnZeroForNullCredit() {
        BigDecimal result = calculator.calculate(null);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
}
