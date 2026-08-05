package com.investmenttracker.analyticsservice.calculator;

import com.investmenttracker.analyticsservice.dto.CreditStatus;
import com.investmenttracker.analyticsservice.model.CreditSnapshot;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RemainingCreditCalculator {
    public BigDecimal calculate(CreditSnapshot snapshot) {
        if (snapshot == null) {
            return BigDecimal.ZERO;
        }
        if (snapshot.status() != CreditStatus.ACTIVE) {
            return BigDecimal.ZERO;
        }
        return snapshot.remainingPrincipalAmount();
    }
}
