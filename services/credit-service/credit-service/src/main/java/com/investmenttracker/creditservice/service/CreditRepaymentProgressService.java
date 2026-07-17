package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntry;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntryStatus;
import com.investmenttracker.creditservice.model.RepaymentProgress;
import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditRepaymentProgressService {
CreditPaymentService creditPaymentService;
RepaymentScheduleService repaymentScheduleService;


public CreditRepaymentProgressService(CreditPaymentService creditPaymentService, RepaymentScheduleService repaymentScheduleService) {
    this.creditPaymentService = creditPaymentService;
    this.repaymentScheduleService = repaymentScheduleService;
}
public RepaymentProgress calculateProgress(Credit credit) {
    List<RepaymentScheduleEntry> entries = repaymentScheduleService.getRepaymentScheduleEntries(credit);
    List<RepaymentScheduleEntry> pendingEntries = entries.stream()
            .filter(entry -> entry.getStatus() == RepaymentScheduleEntryStatus.PENDING)
            .collect(Collectors.toList());
    List<RepaymentScheduleEntry> paidEntries = entries.stream()
            .filter(entry -> entry.getStatus() == RepaymentScheduleEntryStatus.PAID)
            .collect(Collectors.toList());
}
}
