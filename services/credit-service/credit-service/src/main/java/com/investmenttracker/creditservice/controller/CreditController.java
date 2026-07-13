package com.investmenttracker.creditservice.controller;

import com.investmenttracker.creditservice.dto.request.CreateCreditRequest;
import com.investmenttracker.creditservice.dto.request.UpdateCreditRequest;
import com.investmenttracker.creditservice.dto.response.CreditResponse;
import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.mapper.CreditMapper;
import com.investmenttracker.creditservice.service.CreditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;
    private final CreditMapper creditMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditResponse createCredit(@Valid @RequestBody CreateCreditRequest request) {
        Long id = 1L;
        Credit credit = creditMapper.toEntity(request, id);
        Credit savedCredit = creditService.createCredit(credit);
        return creditMapper.toResponse(savedCredit);
    }
    @GetMapping("/me")
    public CreditResponse getCredit(){
        Long userId = 1L;
        return creditMapper.toResponse(creditService.getCreditByUserId(userId));
    }
    @PutMapping("/me")
    public CreditResponse updateCredit(@Valid @RequestBody UpdateCreditRequest request) {
        Long userId = 1L;
        return creditMapper.toResponse(creditService.updateCredit(userId, request));
    }
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCredit(Long userId) {
        creditService.deleteCredit(userId);
    }

}
