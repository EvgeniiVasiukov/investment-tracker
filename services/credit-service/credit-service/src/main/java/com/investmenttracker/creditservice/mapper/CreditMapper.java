package com.investmenttracker.creditservice.mapper;

import com.investmenttracker.creditservice.dto.request.CreateCreditRequest;
import com.investmenttracker.creditservice.entity.Credit;
import org.springframework.stereotype.Component;

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

}
