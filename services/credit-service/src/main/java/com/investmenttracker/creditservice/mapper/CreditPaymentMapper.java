package com.investmenttracker.creditservice.mapper;

import com.investmenttracker.creditservice.dto.response.CreditPaymentResponse;
import com.investmenttracker.creditservice.entity.CreditPayment;
import org.springframework.stereotype.Component;


@Component
public class CreditPaymentMapper {
    public CreditPaymentResponse toResponse(CreditPayment entity) {
        return new CreditPaymentResponse(
                entity.getId(),
                entity.getAmount(),
                entity.getPrincipalAmount(),
                entity.getInterestAmount(),
                entity.getPaymentType(),
                entity.getPaymentSource(),
                entity.getPaymentDate()
        );
    }
}
