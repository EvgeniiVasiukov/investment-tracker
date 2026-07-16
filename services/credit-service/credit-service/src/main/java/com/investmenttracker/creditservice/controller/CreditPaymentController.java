package com.investmenttracker.creditservice.controller;

import com.investmenttracker.creditservice.dto.request.CreateRegularPaymentRequest;
import com.investmenttracker.creditservice.dto.response.CreditPaymentResponse;
import com.investmenttracker.creditservice.entity.CreditPayment;
import com.investmenttracker.creditservice.service.CreditPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit-payments")
public class CreditPaymentController {
    private final CreditPaymentService creditPaymentService;

    public CreditPaymentController(CreditPaymentService creditPaymentService) {
        this.creditPaymentService = creditPaymentService;
    }
    @PostMapping("/regular")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditPaymentResponse createRegularPayment(@RequestBody CreateRegularPaymentRequest request) {
        return creditPaymentService.createRegularPayment(request);
    }
}
