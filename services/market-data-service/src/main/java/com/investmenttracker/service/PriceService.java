package com.investmenttracker.service;

import com.investmenttracker.client.MarketDataClient;
import com.investmenttracker.dto.PriceDto;
import com.investmenttracker.exception.MarketDataNotFoundExceprion;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final MarketDataClient marketDataClient;
    public PriceService(MarketDataClient marketDataClient) {
        this.marketDataClient = marketDataClient;
    }

    public PriceDto getPrice(String ticker) {
        PriceDto price = marketDataClient.getPrice(ticker);
        if (price == null) {
            throw new MarketDataNotFoundExceprion("Price for ticker " + ticker + " not found");
        }
        return price;
    }
}
