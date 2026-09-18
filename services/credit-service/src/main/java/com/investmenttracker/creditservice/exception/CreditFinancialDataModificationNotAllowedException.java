package com.investmenttracker.creditservice.exception;

public class CreditFinancialDataModificationNotAllowedException extends RuntimeException {
    public CreditFinancialDataModificationNotAllowedException(String message) {
        super(message);
    }
}
