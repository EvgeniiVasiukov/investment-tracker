package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.dto.request.CreateRegularPaymentRequest;
import com.investmenttracker.creditservice.dto.request.EarlyRepaymentRequest;
import com.investmenttracker.creditservice.dto.response.CreditPaymentResponse;
import com.investmenttracker.creditservice.entity.*;
import com.investmenttracker.creditservice.exception.CreditAlreadyClosedException;
import com.investmenttracker.creditservice.exception.PaymentExceedsRemainingDebtException;
import com.investmenttracker.creditservice.mapper.CreditPaymentMapper;
import com.investmenttracker.creditservice.repository.CreditPaymentRepository;
import com.investmenttracker.creditservice.repository.CreditRepository;
import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final RepaymentScheduleService repaymentScheduleService;


    public CreditPaymentService(CurrentUserService userService, CreditRepository creditRepository, CreditService creditService, RepaymentScheduleEntryRepository repository, CreditPaymentRepository creditPaymentRepository, CreditPaymentMapper creditPaymentMapper, RepaymentScheduleService repaymentScheduleService) {
        this.userService = userService;
        this.creditRepository = creditRepository;
        this.creditService = creditService;
        this.repaymentScheduleEntryRepository = repository;
        this.creditPaymentRepository = creditPaymentRepository;
        this.creditPaymentMapper = creditPaymentMapper;
        this.repaymentScheduleService = repaymentScheduleService;
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
        RepaymentScheduleEntry firstPendingEntry = repaymentScheduleEntryRepository.findFirstByCreditAndStatusOrderByInstallmentNumberAsc(credit, PENDDING)
                .orElseThrow(() -> new CreditAlreadyClosedException("Credit was closed there is no payments left"));
        BigDecimal currentRemainingPrincipalAmount = firstPendingEntry.getPrincipalAmount().add(firstPendingEntry.getRemainingPrincipalAmount());
        if (request.amount().compareTo(currentRemainingPrincipalAmount) > 0) {
            throw new PaymentExceedsRemainingDebtException("Early payment is larger than the remaining debt");
        }
        CreditPayment payment = new CreditPayment();
        payment.setPaymentType(CreditPaymentType.EARLY_PAYMENT);
        payment.setAmount(request.amount());
        payment.setPrincipalAmount(request.amount());
        payment.setInterestAmount(BigDecimal.ZERO);
        payment.setPaymentDate(request.paymentDate());
        payment.setPaymentSource(request.source());
        payment.setCredit(credit);

        List<RepaymentScheduleEntry> pendingEntries = repaymentScheduleEntryRepository.findByCreditAndStatusOrderByInstallmentNumberAsc(
                credit, PENDDING);

        BigDecimal remainingPrincipalAmountAfterEarlyPayment = currentRemainingPrincipalAmount.subtract(request.amount());
        if (remainingPrincipalAmountAfterEarlyPayment.compareTo(BigDecimal.ZERO) == 0) {
            creditPaymentRepository.save(payment);
            repaymentScheduleEntryRepository.deleteAll(pendingEntries);
            credit.setStatus(CreditStatus.CLOSED);
            creditRepository.save(credit);
            return creditPaymentMapper.toResponse(payment);
        }
        BigDecimal remainingPrincipal = remainingPrincipalAmountAfterEarlyPayment;
        BigDecimal monthlyRate = repaymentScheduleService.calculateMonthlyRate(credit.getAnnualInterestRate());
        List<RepaymentScheduleEntry> entriesToDelete = new ArrayList<>();
        boolean debtFullyRepaid = false;
        for (RepaymentScheduleEntry entry : pendingEntries) {
            if (debtFullyRepaid) {
                entriesToDelete.add(entry);
                continue;
            }
            BigDecimal interestAmount = repaymentScheduleService.calculateInterest(remainingPrincipal, monthlyRate);
            BigDecimal principalAmount = repaymentScheduleService.calculatePrincipal(credit.getMonthlyPayment(), interestAmount);
            if (principalAmount.compareTo(remainingPrincipal) > 0) {
                principalAmount = remainingPrincipal;
            }
            remainingPrincipal = remainingPrincipal.subtract(principalAmount);

            repaymentScheduleService.updateRepaymentScheduleEntry(entry, interestAmount, principalAmount, remainingPrincipal);
            if (remainingPrincipal.compareTo(BigDecimal.ZERO) == 0) {
                debtFullyRepaid = true;
            }
        }

        creditPaymentRepository.save(payment);
        repaymentScheduleEntryRepository.deleteAll(entriesToDelete);
        return creditPaymentMapper.toResponse(payment);
    }
//method for controller returns CreditPaymentsResponses
    public List<CreditPaymentResponse> getPaymentHistory(){

        return getCreditPayments()
                .stream()
                .map(creditPaymentMapper::toResponse)
                .collect(Collectors.toList());

    }
// method for other services
    public List<CreditPayment> getCreditPayments() {
        List<CreditPayment> payments = creditPaymentRepository.findByCreditOrderByPaymentDateAsc(getUserCredit());
        return payments;
    }

    private Credit getUserCredit() {
        return creditService.getCurrentUserCredit(userService.getCurrentUserId());
    }
}
