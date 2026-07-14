package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.dto.request.CreateRegularPaymentRequest;
import com.investmenttracker.creditservice.dto.response.CreditPaymentResponse;
import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.CreditPayment;
import com.investmenttracker.creditservice.entity.CreditPaymentType;
import com.investmenttracker.creditservice.mapper.CreditPaymentMapper;
import com.investmenttracker.creditservice.repository.CreditPaymentReoisitory;
import com.investmenttracker.creditservice.repository.CreditRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CreditPaymentService {
    private final CurrentUserService userService;
    private final CreditRepository creditRepository;
    private final CreditService creditService;
    private final CreditPaymentReoisitory repository;

    public CreditPaymentService(CurrentUserService userService, CreditRepository creditRepository, CreditService creditService, CreditPaymentReoisitory repository) {
        this.userService = userService;
        this.creditRepository = creditRepository;
        this.creditService = creditService;
        this.repository = repository;
    }

    CreditPaymentResponse createRegularPayment(CreateRegularPaymentRequest request) {
        Credit credit = creditService.getCreditByUserId(userService.getCurrentUserId());
        List<CreditPayment> payments = repository.findByCreditIdOrderByPaymentDateAsc(credit.getId());
        BigDecimal paidPrincipal = payments.stream()
                .map(CreditPayment::getPrincipalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remainingPrincipal = credit.getPrincipalAmount().subtract(paidPrincipal);
        BigDecimal monthlyRate = credit.getAnnualInterestRate()
                .divide(BigDecimal.valueOf(100), 10, BigDecimal.ROUND_HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal interestAmount = remainingPrincipal
                .multiply(monthlyRate)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal principalAmount = credit.getMonthlyPayment().subtract(interestAmount);

        CreditPayment creditPayment = new CreditPayment();
        creditPayment.setCredit(credit);
        creditPayment.setAmount(credit.getMonthlyPayment());
        creditPayment.setPrincipalAmount(principalAmount);
        creditPayment.setInterestAmount(interestAmount);
        creditPayment.setPaymentDate(request.paymentDate());
        creditPayment.setPaymentType(CreditPaymentType.REGULAR);
        creditPayment.setPaymentSource(request.source());
    }
}
