package com.investmenttracker.creditservice.dto.request;

import com.investmenttracker.creditservice.entity.CreditPaymentSource;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EarlyRepaymentRequest {
    @NotNull
    BigDecimal amount;
    @NotNull
    LocalDateTime paymentDate;
    @NotNull
    CreditPaymentSource source;
}
