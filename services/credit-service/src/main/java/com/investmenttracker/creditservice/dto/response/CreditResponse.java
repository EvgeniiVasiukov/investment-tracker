package com.investmenttracker.creditservice.dto.response;

import com.investmenttracker.creditservice.entity.CreditStatus;

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
