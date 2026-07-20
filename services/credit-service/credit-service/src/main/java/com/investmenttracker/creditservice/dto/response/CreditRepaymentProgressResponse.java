package com.investmenttracker.creditservice.dto.response;

import com.investmenttracker.creditservice.entity.CreditStatus;

import java.math.BigDecimal;

public record CreditRepaymentProgressResponse(
        BigDecimal remainingPrincipal,
        BigDecimal totalPrincipalPaid,
        BigDecimal totalInterestPaid,
        BigDecimal totalAmountPaid,
        Integer remainingPayments,
        BigDecimal repaymentPercentage,
        CreditStatus creditStatus
) {
}
