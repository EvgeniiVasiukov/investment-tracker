package com.investmenttracker.client;

import com.investmenttracker.dto.PriceDto;

public interface MarketDataClient {
    PriceDto getPrice(String ticker);
}
