package com.investmenttracker.creditservice.dto.request;

import com.investmenttracker.creditservice.entity.CreditPaymentSource;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EarlyRepaymentRequest (
    @NotNull
    BigDecimal amount,
    @NotNull
    LocalDate paymentDate,
    @NotNull
    CreditPaymentSource source) {
}
