package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.dto.request.CreateRegularPaymentRequest;
import com.investmenttracker.creditservice.dto.request.EarlyRepaymentRequest;
import com.investmenttracker.creditservice.dto.response.CreditPaymentResponse;
import com.investmenttracker.creditservice.entity.*;
import com.investmenttracker.creditservice.exception.CreditAlreadyClosedException;
import com.investmenttracker.creditservice.mapper.CreditPaymentMapper;
import com.investmenttracker.creditservice.repository.CreditPaymentRepository;
import com.investmenttracker.creditservice.repository.CreditRepository;
import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditPaymentService {
    private final CurrentUserService userService;
    private final CreditRepository creditRepository;
    private final CreditService creditService;
    private final RepaymentScheduleEntryRepository repaymentScheduleEntryRepository;
    private final CreditPaymentRepository creditPaymentRepository;
    private final CreditPaymentMapper creditPaymentMapper;
    private static final RepaymentScheduleEntryStatus PENDDING = RepaymentScheduleEntryStatus.PENDING;


    public CreditPaymentService(CurrentUserService userService, CreditRepository creditRepository, CreditService creditService, RepaymentScheduleEntryRepository repository, CreditPaymentRepository creditPaymentRepository, CreditPaymentMapper creditPaymentMapper) {
        this.userService = userService;
        this.creditRepository = creditRepository;
        this.creditService = creditService;
        this.repaymentScheduleEntryRepository = repository;
        this.creditPaymentRepository = creditPaymentRepository;
        this.creditPaymentMapper = creditPaymentMapper;
    }

    @Transactional
    public CreditPaymentResponse createRegularPayment(CreateRegularPaymentRequest request) {
        Credit credit = getUserCredit();
        RepaymentScheduleEntry nextRepaymentEntry = repaymentScheduleEntryRepository.findFirstByCreditAndStatusOrderByInstallmentNumberAsc(credit, PENDDING)
                .orElseThrow(() -> new CreditAlreadyClosedException("Credit already closed, cannot create payment"));

        BigDecimal interestAmount = nextRepaymentEntry.getInterestAmount();
        BigDecimal principalAmount = nextRepaymentEntry.getPrincipalAmount();
        BigDecimal totalPaymentAmount = nextRepaymentEntry.getTotalPaymentAmount();

        CreditPayment payment = new CreditPayment();
        payment.setAmount(totalPaymentAmount);
        payment.setInterestAmount(interestAmount);
        payment.setPrincipalAmount(principalAmount);
        payment.setCredit(credit);
        payment.setPaymentType(CreditPaymentType.REGULAR);
        payment.setPaymentDate(request.paymentDate());
        payment.setPaymentSource(request.source());

        nextRepaymentEntry.setStatus(RepaymentScheduleEntryStatus.PAID);

        creditPaymentRepository.save(payment);
        repaymentScheduleEntryRepository.save(nextRepaymentEntry);

        return creditPaymentMapper.toResponse(payment);
    }

    @Transactional
    public CreditPaymentResponse createEarlyPayment(EarlyRepaymentRequest request) {
        Credit credit = getUserCredit();

    }

    public List<CreditPaymentResponse> getPaymentHistory(){
        Credit credit = getUserCredit();
        List<CreditPayment> payments = creditPaymentRepository.findByCreditOrderByPaymentDateAsc(credit);
        return payments.stream()
                .map(creditPaymentMapper::toResponse)
                .collect(Collectors.toList());

    }

    private Credit getUserCredit() {
        return creditService.getCurrentUserCredit(userService.getCurrentUserId());
    }
}
