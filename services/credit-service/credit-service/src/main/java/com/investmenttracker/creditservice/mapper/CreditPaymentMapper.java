package com.investmenttracker.creditservice.mapper;

import com.investmenttracker.creditservice.dto.response.CreditPaymentResponse;
import com.investmenttracker.creditservice.entity.CreditPayment;

public interface CreditPaymentMapper {
    CreditPaymentResponse toResponse(CreditPayment entity);
}
