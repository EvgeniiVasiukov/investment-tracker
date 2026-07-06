package com.investmenttracker.userservice.exception;

public class SelfModificationNotAllowedException extends RuntimeException {
    public SelfModificationNotAllowedException(String message) {
        super(message);
    }
}
