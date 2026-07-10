package com.investmenttracker.creditservice.mapper;

import com.investmenttracker.creditservice.dto.request.CreateCreditRequest;
import com.investmenttracker.creditservice.dto.response.CreditResponse;
import com.investmenttracker.creditservice.entity.Credit;
import org.springframework.stereotype.Component;

import java.net.CacheResponse;

@Component
public class CreditMapper {
    public Credit toEntity(CreateCreditRequest request, Long userId) {
        Credit credit = new Credit();
        credit.setUserId(userId);
        credit.setPrincipalAmount(request.principalAmount());
        credit.setBankName(request.bankName());
        credit.setAnnualInterestRate(request.annualInterestRate());
        credit.setTermMonths(request.termMonths());
        credit.setMonthlyPayment(request.monthlyPayment());
        credit.setStartDate(request.startDate());
        return credit;
    }
    public CreditResponse toResponse(Credit credit) {
        return new CreditResponse(
                credit.getId(),
                credit.getBankName(),
                credit.getPrincipalAmount(),
                credit.getAnnualInterestRate(),
                credit.getTermMonths(),
                credit.getMonthlyPayment(),
                credit.getStartDate(),
                credit.getStatus(),
                credit.getCreatedAt(),
                credit.getUpdatedAt()
        );
    }
}
