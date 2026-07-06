package com.investmenttracker.userservice.exception;

public class UserIsBlockedException extends RuntimeException {
    public UserIsBlockedException(String message) {
        super(message);
    }
}
