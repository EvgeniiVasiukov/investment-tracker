package com.investmenttracker.creditservice.exception;

public class CreditAlreadyExistsException extends RuntimeException {
    public CreditAlreadyExistsException(String message) {
        super(message);
    }
}
