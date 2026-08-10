package com.investmenttracker.exception;

public class InsufficientPositionQuantityException extends RuntimeException {
    public InsufficientPositionQuantityException(String message) {
        super(message);
    }
}
