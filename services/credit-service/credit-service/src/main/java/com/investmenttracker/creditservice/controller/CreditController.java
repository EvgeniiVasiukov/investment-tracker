package com.investmenttracker.creditservice.controller;

import com.investmenttracker.creditservice.dto.request.CreateCreditRequest;
import com.investmenttracker.creditservice.dto.response.CreditResponse;
import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.mapper.CreditMapper;
import com.investmenttracker.creditservice.service.CreditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;
    private final CreditMapper creditMapper;

    @PostMapping
    public CreditResponse createCredit(@Valid @RequestBody CreateCreditRequest request) {
        Long id = 1L;
        Credit credit = creditMapper.toEntity(request, id);
        Credit savedCredit = creditService.createCredit(credit);
        return creditMapper.toResponse(savedCredit);
    }

}
