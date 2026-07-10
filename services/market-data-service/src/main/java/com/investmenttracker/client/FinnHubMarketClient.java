package com.investmenttracker.client;

import com.investmenttracker.client.dto.AlphaVantageGlobalQuoteResponse;
import com.investmenttracker.client.dto.FinnHubResponse;
import com.investmenttracker.config.AlphaVantageProperties;
import com.investmenttracker.config.FinnHubProperties;
import com.investmenttracker.dto.PriceDto;
import com.investmenttracker.exception.MarketDataProviderException;
import com.investmenttracker.model.Currency;
import com.investmenttracker.model.MarketDataProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;

@Component("finnhubMarketClient")
public class FinnHubMarketClient implements MarketDataClient {
    private final FinnHubProperties properties;
    private final RestClient restClient;

    public FinnHubMarketClient(FinnHubProperties properties,
                               @Qualifier("finnhubRestClient") RestClient restClient) {
        this.properties = properties;
        this.restClient = restClient;
    }

    @Override
    public PriceDto getPrice(String ticker) {
        try {
            FinnHubResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/quote")
                            .queryParam("symbol",ticker)
                            .queryParam("token", properties.getApiKey())
                            .build())
                    .retrieve()
                    .body(FinnHubResponse.class);
            validateResponse(response);
            return toPriceDto(response, ticker);
        } catch (RestClientException e) {
            throw new MarketDataProviderException("Failed to retrieve data from FinnHub API", e);
        }
    }

    private void validateResponse(FinnHubResponse response) {
        if (response == null || response.currentPrice() == null) {
            throw new MarketDataProviderException("Received invalid response from FinnHub API");
        }
    }

    private PriceDto toPriceDto(FinnHubResponse response, String ticker) {
        return  new PriceDto(
                ticker,
                response.currentPrice(),
                Currency.USD,
                MarketDataProvider.FINNHUB,
                Instant.now());
    }
}
