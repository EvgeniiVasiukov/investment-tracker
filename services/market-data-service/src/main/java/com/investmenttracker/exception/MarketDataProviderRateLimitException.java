package com.investmenttracker.exception;

public class MarketDataProviderRateLimitException extends RuntimeException {
    public MarketDataProviderRateLimitException(String message) {
        super(message);
    }
}
