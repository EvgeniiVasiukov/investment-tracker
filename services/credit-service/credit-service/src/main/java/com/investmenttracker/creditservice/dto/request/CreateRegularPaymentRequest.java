package com.investmenttracker.creditservice.dto.request;

import com.investmenttracker.creditservice.entity.CreditPaymentSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateRegularPaymentRequest(
        @NotNull
        LocalDate paymentDate,
        @NotNull
        CreditPaymentSource source
) {
}
