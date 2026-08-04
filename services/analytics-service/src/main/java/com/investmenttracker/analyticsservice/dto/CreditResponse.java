package com.investmenttracker.analyticsservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreditResponse(
        Long id,
        String bankName,
        BigDecimal principalAmount,
        BigDecimal annualInterestRate,
        Integer termMonths,
        BigDecimal monthlyPayment,
        LocalDate startDate,
        CreditStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
