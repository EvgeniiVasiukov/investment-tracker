package com.investmenttracker.creditservice.dto.request;

import com.investmenttracker.creditservice.entity.CreditPaymentSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateRegularPaymentRequest(
        @NotNull
        LocalDateTime paymentDate,
        @NotNull
        CreditPaymentSource source;
) {
}
