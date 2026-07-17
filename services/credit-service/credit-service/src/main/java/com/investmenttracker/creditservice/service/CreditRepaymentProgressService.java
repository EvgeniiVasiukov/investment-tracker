package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.CreditPayment;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntry;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntryStatus;
import com.investmenttracker.creditservice.model.RepaymentProgress;
import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CreditRepaymentProgressService {
CreditPaymentService creditPaymentService;
RepaymentScheduleService repaymentScheduleService;
private static BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);


public CreditRepaymentProgressService(CreditPaymentService creditPaymentService, RepaymentScheduleService repaymentScheduleService) {
    this.creditPaymentService = creditPaymentService;
    this.repaymentScheduleService = repaymentScheduleService;
}
public RepaymentProgress calculateProgress(Credit credit) {
    List<RepaymentScheduleEntry> entries = repaymentScheduleService.getRepaymentScheduleEntries(credit);
    List<RepaymentScheduleEntry> pendingEntries = entries.stream()
            .filter(entry -> entry.getStatus() == RepaymentScheduleEntryStatus.PENDING)
            .collect(Collectors.toList());
    Integer remainingPayments = pendingEntries.size();
    BigDecimal remainingPrincipal;
    if (remainingPayments == 0) {
        remainingPrincipal = BigDecimal.ZERO;
    } else {
        remainingPrincipal = pendingEntries.get(0).getRemainingPrincipalAmount().add(pendingEntries.get(0).getPrincipalAmount());
    }
    List<CreditPayment> creditPayments = creditPaymentService.getCreditPayments();
    BigDecimal totalPaid = sumPayments(creditPayments, CreditPayment::getAmount);
    BigDecimal interestPaid = sumPayments(creditPayments, CreditPayment::getInterestAmount);
    BigDecimal principalPaid = sumPayments(creditPayments, CreditPayment::getPrincipalAmount);
    BigDecimal repaymentPercentage = (principalPaid.multiply(ONE_HUNDRED)
            .divide(credit.getPrincipalAmount(), 2, BigDecimal.ROUND_HALF_UP))
            ;

 return RepaymentProgress.builder()
        .repaymentPercentage(repaymentPercentage)
        .principalPaid(principalPaid)
        .interestPaid(interestPaid)
        .totalPaid(totalPaid)
        .remainingPayments(remainingPayments)
        .remainingPrincipal(remainingPrincipal)
        .build();
}

private BigDecimal sumPayments(List<CreditPayment> creditPayments, Function<CreditPayment, BigDecimal> mapper) {
    return creditPayments.stream().map(mapper).reduce(BigDecimal.ZERO, BigDecimal::add);

}
}
