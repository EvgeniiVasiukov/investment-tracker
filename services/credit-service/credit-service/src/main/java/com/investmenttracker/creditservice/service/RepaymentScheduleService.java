package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntry;
import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepaymentScheduleService {
    private final RepaymentScheduleEntryRepository repository;
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private static final RoundingMode HALF_UP = RoundingMode.HALF_UP;


    public RepaymentScheduleService(RepaymentScheduleEntryRepository repository) {
        this.repository = repository;
    }

    public List<RepaymentScheduleEntry> generateSchedule(Credit credit){
        List<RepaymentScheduleEntry> scheduleEntries = new ArrayList<>();
        BigDecimal remainingPrincipalAmount = credit.getPrincipalAmount();
        BigDecimal monthlyRate = credit.getAnnualInterestRate().divide(ONE_HUNDRED, 10, HALF_UP).divide(MONTHS_IN_YEAR, 10, HALF_UP);
        LocalDate issueDate = credit.getStartDate();
        LocalDate firstPaymentDate = calculateFirstPaymentDate(issueDate);
        for (int i = 1; i <= credit.getTermMonths(); i++) {
            BigDecimal interestAmount = remainingPrincipalAmount.multiply(monthlyRate).setScale(2, HALF_UP);
            BigDecimal principalAmount = credit.getMonthlyPayment().subtract(interestAmount);
            BigDecimal totalPaymentAmount = credit.getMonthlyPayment();
            LocalDate targetMonth = firstPaymentDate.plusMonths(i-1);
            LocalDate paymentDate = targetMonth.withDayOfMonth(Math.min(30, targetMonth.lengthOfMonth()));
            if (principalAmount.compareTo(remainingPrincipalAmount) > 0) {
                principalAmount = remainingPrincipalAmount;
                totalPaymentAmount = principalAmount.add(interestAmount);
            }
            remainingPrincipalAmount = remainingPrincipalAmount.subtract(principalAmount);
            RepaymentScheduleEntry repaymentScheduleEntry = new RepaymentScheduleEntry();
            repaymentScheduleEntry.setCredit(credit);
            repaymentScheduleEntry.setInstallmentNumber(i);
            repaymentScheduleEntry.setPaymentDate(paymentDate);
            repaymentScheduleEntry.setTotalPaymentAmount(totalPaymentAmount);
            repaymentScheduleEntry.setInterestAmount(interestAmount);
            repaymentScheduleEntry.setPrincipalAmount(principalAmount);
            repaymentScheduleEntry.setRemainingPrincipalAmount(remainingPrincipalAmount);
            scheduleEntries.add(repaymentScheduleEntry);
        }
        return repository.saveAll(scheduleEntries);

    }
    private LocalDate calculateFirstPaymentDate(LocalDate issueDate) {
        LocalDate firstPaymentDate;
        if (issueDate.getDayOfMonth() < 20) {
            int day = Math.min(30, issueDate.lengthOfMonth());
            firstPaymentDate = issueDate.withDayOfMonth(day);
        } else {
            LocalDate nextMonth = issueDate.plusMonths(1);
            int day = nextMonth.lengthOfMonth();
            firstPaymentDate = nextMonth.withDayOfMonth(day);
        }
        return firstPaymentDate;
    }
}
