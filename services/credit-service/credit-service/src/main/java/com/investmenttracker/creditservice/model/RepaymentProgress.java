package com.investmenttracker.creditservice.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentProgress {
    BigDecimal remainingPrincipal;
    BigDecimal totalPaid;
    BigDecimal principalPaid;
    BigDecimal interestPaid;
    BigDecimal remainingPayments;
    BigDecimal remainingPercentage;
}
