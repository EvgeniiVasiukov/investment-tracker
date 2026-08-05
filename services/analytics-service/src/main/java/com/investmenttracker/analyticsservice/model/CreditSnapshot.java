package com.investmenttracker.analyticsservice.model;

import com.investmenttracker.analyticsservice.dto.CreditStatus;

import java.math.BigDecimal;

public record CreditSnapshot(
        BigDecimal remainingPrincipalAmount,
        CreditStatus status
) {
}
