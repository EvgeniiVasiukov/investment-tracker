package com.investmenttracker.client;

import com.investmenttracker.dto.PriceDto;
import com.investmenttracker.model.Currency;
import com.investmenttracker.model.MarketDataProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class MockMarketDataClient implements MarketDataClient {
    private final Map<String, PriceDto> prices = new HashMap<>();
    public MockMarketDataClient() {
        prices.put("NVDA", new PriceDto("NVDA", BigDecimal.valueOf(180.50), Currency.EUR, MarketDataProvider.GOOGLE, Instant.now() ));
        prices.put("APPL", new PriceDto("APPL", BigDecimal.valueOf(210.75), Currency.EUR, MarketDataProvider.YAHOO, Instant.now()));
        prices.put("TSMC", new PriceDto("TSMC", BigDecimal.valueOf(240.10), Currency.USD, MarketDataProvider.GOOGLE, Instant.now()));
    }
    @Override
    public PriceDto getPrice(String ticker) {
        return prices.get(ticker);
    }
}
