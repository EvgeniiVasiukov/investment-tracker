package com.investmenttracker.creditservice.exception;

public class CreditAlreadyClosedException extends RuntimeException {
    public CreditAlreadyClosedException(String message) {
        super(message);
    }
}
