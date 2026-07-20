package com.investmenttracker.creditservice.mapper;

import com.investmenttracker.creditservice.dto.response.CreditRepaymentProgressResponse;
import com.investmenttracker.creditservice.entity.CreditStatus;
import com.investmenttracker.creditservice.model.RepaymentProgress;

public class CreditRepaymentMapper {
    public CreditRepaymentProgressResponse toResponse(RepaymentProgress progress, CreditStatus creditStatus) {
        return new CreditRepaymentProgressResponse(
                progress.getRemainingPrincipal(),
                progress.getPrincipalPaid(),
                progress.getInterestPaid(),
                progress.getTotalPaid(),
                progress.getRemainingPayments(),
                progress.getRepaymentPercentage(),
                creditStatus
        );
    }
}
