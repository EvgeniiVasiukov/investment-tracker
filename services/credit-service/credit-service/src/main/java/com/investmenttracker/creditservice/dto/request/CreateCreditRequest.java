package com.investmenttracker.creditservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCreditRequest(
        @NotBlank
        String bankName,
        @NotNull
        @Positive
        @DecimalMin("1000.00")
        BigDecimal principalAmount,
                @NotNull
                @Positive
        BigDecimal annualInterestRate,
                @NotNull
                @Positive
        Integer termMonths,
                @NotNull
                @Positive
                @DecimalMin("500.00")
        BigDecimal monthlyPayment,
                @NotNull
        LocalDate startDate
) {
}
