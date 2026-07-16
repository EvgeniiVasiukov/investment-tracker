package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.dto.request.UpdateCreditRequest;
import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.CreditStatus;
import com.investmenttracker.creditservice.exception.CreditAlreadyExistsException;
import com.investmenttracker.creditservice.exception.CreditFinancialDataModificationNotAllowedException;
import com.investmenttracker.creditservice.exception.CreditNotFoundException;
import com.investmenttracker.creditservice.mapper.CreditMapper;
import com.investmenttracker.creditservice.repository.CreditPaymentRepository;
import com.investmenttracker.creditservice.repository.CreditRepository;
import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CreditService {
    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;
    private final RepaymentScheduleService repaymentScheduleService;
    private final CreditPaymentRepository creditPaymentRepository;

    public CreditService(CreditRepository creditRepository, CreditMapper creditMapper, RepaymentScheduleService repaymentScheduleService, CreditPaymentRepository creditPaymentRepository) {
        this.creditRepository = creditRepository;
        this.creditMapper = creditMapper;
        this.repaymentScheduleService = repaymentScheduleService;
        this.creditPaymentRepository = creditPaymentRepository;
    }

    @Transactional
    public Credit createCredit(Credit credit) {
       if (creditRepository.existsById(credit.getUserId())) {
           throw new CreditAlreadyExistsException("User already has credit associated with this id");
       }
       credit.setStatus(CreditStatus.ACTIVE);
        Credit saved = creditRepository.save(credit);
        repaymentScheduleService.generateSchedule(saved);
       return saved;
   }

    public Credit getCurrentUserCredit(Long userId) {
       return creditRepository.findByUserId(userId)
               .orElseThrow(()-> new CreditNotFoundException("Credit for user was not found"));
    }
    public Credit updateCredit(Long userId, UpdateCreditRequest request) {
           Credit credit = getCurrentUserCredit(userId);
        boolean hasPayments = creditPaymentRepository.existsByCredit(credit);
        if (hasPayments) {
            boolean financialFieldsChanged = (credit.getMonthlyPayment().compareTo(request.monthlyPayment()) != 0)
                    || !(credit.getTermMonths().equals(request.termMonths())
            || (credit.getAnnualInterestRate().compareTo(request.annualInterestRate()) != 0)
                    || (credit.getPrincipalAmount().compareTo(request.principalAmount()) != 0));
            if (financialFieldsChanged) {
                throw new CreditFinancialDataModificationNotAllowedException("Financial credit parameters cannot be modified after the first payment");
            }
        }

        creditMapper.updateEntity(credit, request);
            return creditRepository.save(credit);
    }
    public void deleteCredit(Long userId) {
        Credit credit = getCurrentUserCredit(userId);
        creditRepository.delete(credit);
    }

}

