package com.investmenttracker.creditservice.dto.response;

import com.investmenttracker.creditservice.entity.CreditPaymentSource;
import com.investmenttracker.creditservice.entity.CreditPaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditPaymentResponse(
        Long id,
        BigDecimal amount,
        BigDecimal principalAmount,
        BigDecimal interestAmount,
        CreditPaymentType paymentType,
        CreditPaymentSource paymentSource,
        LocalDate paymentDate
) {
}
