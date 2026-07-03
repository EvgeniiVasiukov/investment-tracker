package com.investmenttracker.exception;

public class PositionAccessDeniedException extends RuntimeException {
    public PositionAccessDeniedException(String message) {
        super(message);
    }
}
