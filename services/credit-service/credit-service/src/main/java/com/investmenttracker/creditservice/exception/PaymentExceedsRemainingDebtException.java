package com.investmenttracker.creditservice.exception;

public class PaymentExceedsRemainingDebtException extends RuntimeException {
    public PaymentExceedsRemainingDebtException(String message) {
        super(message);
    }
}
