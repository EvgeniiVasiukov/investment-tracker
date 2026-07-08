package com.investmenttracker.client;

import com.investmenttracker.dto.PriceDto;
import com.investmenttracker.exception.MarketDataProviderRateLimitException;
import com.investmenttracker.exception.NoAvailableMarketDataProviderException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class FallbackMarketDataClient implements MarketDataClient {
    private final List<MarketDataClient> clients;

    public FallbackMarketDataClient(@Qualifier("alphaVantageMarketClient")MarketDataClient alphaVantageMarketClient,
                                    @Qualifier("finnhubMarketClient")MarketDataClient finnhubMarketClient) {
        this.clients = List.of(alphaVantageMarketClient, finnhubMarketClient);
    }

    @Override
    public PriceDto getPrice(String ticker) {
        for (MarketDataClient client : clients) {
            try {
                return client.getPrice(ticker);
            } catch (MarketDataProviderRateLimitException e) {
                //TODO : log that the provider has reached its rate limit
                //Continue with the next provider
            }
        }
        throw new NoAvailableMarketDataProviderException("All market data provider rate limit exceeded");
    }
}
