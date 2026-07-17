package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditRepaymentProgressService {
CreditPaymentService creditPaymentService;
RepaymentScheduleService repaymentScheduleService;


public CreditRepaymentProgressService(CreditPaymentService creditPaymentService, RepaymentScheduleService repaymentScheduleService) {
    this.creditPaymentService = creditPaymentService;
    this.repaymentScheduleService = repaymentScheduleService;
}
}
