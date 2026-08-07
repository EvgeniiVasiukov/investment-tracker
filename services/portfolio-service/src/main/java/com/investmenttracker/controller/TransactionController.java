package com.investmenttracker.controller;

import com.investmenttracker.dto.request.BuyTransactionRequest;
import com.investmenttracker.dto.response.BuyTransactionResponse;
import com.investmenttracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public BuyTransactionResponse buyTransaction(
            @Valid
            @RequestBody BuyTransactionRequest buyTransactionRequest
            ) {
        return transactionService.processBuy(buyTransactionRequest);
    }
}
