package com.investmenttracker.controller;

import com.investmenttracker.dto.request.BuyTransactionRequest;
import com.investmenttracker.dto.request.SellTransactionRequest;
import com.investmenttracker.dto.request.TransactionFilter;
import com.investmenttracker.dto.response.BuyTransactionResponse;
import com.investmenttracker.dto.response.SellTransactionResponse;
import com.investmenttracker.dto.response.TransactionResponse;
import com.investmenttracker.dto.response.TransactionSummaryResponse;
import com.investmenttracker.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Transactions",
        description = "BUY and SELL transaction processing"
)
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Create BUY transaction",
            description = "Creates a BUY transaction for the authenticated user. " +
                    "If the position does not exist, a new position is created. " +
                    "If the position already exists, quantity and weighted average price are updated."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "BUY transaction successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid transaction request"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public BuyTransactionResponse buyTransaction(
            @Valid
            @RequestBody BuyTransactionRequest buyTransactionRequest
            ) {
        return transactionService.processBuy(buyTransactionRequest);
    }

    @Operation(
            summary = "Create SELL transaction",
            description = "Creates a SELL transaction for the authenticated user. " +
                    "The existing position quantity is reduced. " +
                    "If the entire position is sold, the position is removed."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "SELL transaction successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid transaction request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Position not found"
            ),
            @ApiResponse(
                    responseCode = "418",
                    description = "Insufficient position quantity"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PostMapping("/sell")
    @ResponseStatus(HttpStatus.CREATED)
    public SellTransactionResponse sellTransaction(
            @Valid
            @RequestBody SellTransactionRequest sellTransactionRequest
    ) {
        return transactionService.processSell(sellTransactionRequest);
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of filtered transactions received"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid transaction request")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TransactionResponse> getAllTransactions(TransactionFilter filter, Pageable pageable) {
        return transactionService.getAllTransactions(filter, pageable);
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Realized profit/Loss successfully received"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @GetMapping("/summary")
    @ResponseStatus(HttpStatus.OK)
    public TransactionSummaryResponse getTransactionSummary(){
        return transactionService.getTransactionSummary();
    }
}
