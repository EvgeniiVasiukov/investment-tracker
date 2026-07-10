package com.investmenttracker.exception;

public class NoAvailableMarketDataProviderException extends RuntimeException {
    public NoAvailableMarketDataProviderException(String message) {
        super(message);
    }
}
