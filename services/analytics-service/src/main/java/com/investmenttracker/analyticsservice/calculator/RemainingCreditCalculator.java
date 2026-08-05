package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.dto.CreditStatus;
import com.investmenttracker.analyticsservice.model.CreditSnapshot;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RemainingCreditCalculator {
    public BigDecimal calculate(CreditSnapshot credit) {
        if (credit == null) {
            return BigDecimal.ZERO;
        }
        if (credit.status() != CreditStatus.ACTIVE) {
            return BigDecimal.ZERO;
        }
        return credit.remainingPrincipalAmount();
    }
}
