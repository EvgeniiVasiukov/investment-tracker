package com.investmenttracker.creditservice.controller;

import com.investmenttracker.creditservice.dto.request.CreateCreditRequest;
import com.investmenttracker.creditservice.dto.request.UpdateCreditRequest;
import com.investmenttracker.creditservice.dto.response.CreditPaymentResponse;
import com.investmenttracker.creditservice.dto.response.CreditResponse;
import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.mapper.CreditMapper;
import com.investmenttracker.creditservice.service.CreditPaymentService;
import com.investmenttracker.creditservice.service.CreditService;
import com.investmenttracker.creditservice.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;
    private final CreditMapper creditMapper;
    private final CurrentUserService currentUserService;
    private final CreditPaymentService creditPaymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditResponse createCredit(@Valid @RequestBody CreateCreditRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Credit credit = creditMapper.toEntity(request, userId);
        Credit savedCredit = creditService.createCredit(credit);
        return creditMapper.toResponse(savedCredit);
    }
    @GetMapping("/me")
    public CreditResponse getCredit(){
        Long userId = currentUserService.getCurrentUserId();
        return creditMapper.toResponse(creditService.getCurrentUserCredit(userId));
    }
    @GetMapping("/me/payments")
    public List<CreditPaymentResponse> getCreditPayments(){
        return creditPaymentService.getPaymentHistory();
    }
    @PutMapping("/me")
    public CreditResponse updateCredit(@Valid @RequestBody UpdateCreditRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        return creditMapper.toResponse(creditService.updateCredit(userId, request));
    }
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCredit() {
        Long userId = currentUserService.getCurrentUserId();
        creditService.deleteCredit(userId);
    }

}
