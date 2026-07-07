package com.investmenttracker.client;

import com.investmenttracker.client.dto.AlphaVantageGlobalQuote;
import com.investmenttracker.client.dto.AlphaVantageGlobalQuoteResponse;
import com.investmenttracker.config.AlphaVantageProperties;
import com.investmenttracker.dto.PriceDto;
import com.investmenttracker.exception.MarketDataProviderException;
import com.investmenttracker.exception.MarketDataProviderRateLimitException;
import com.investmenttracker.model.Currency;
import com.investmenttracker.model.MarketDataProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.Instant;

@Component
@Primary
public class AlphaVantageMarketClient implements MarketDataClient {
    private final AlphaVantageProperties properties;
    private final RestClient restClient;

    public AlphaVantageMarketClient(AlphaVantageProperties properties,
                                    @Qualifier("alphaVantageRestClient") RestClient restClient) {
        this.properties = properties;
        this.restClient = restClient;
    }

    @Override
    public PriceDto getPrice(String ticker) {
       try {
           AlphaVantageGlobalQuoteResponse response = restClient.get()
               .uri(uriBuilder -> uriBuilder
                       .path("/query")
                       .queryParam("function", "GLOBAL_QUOTE")
                       .queryParam("symbol", ticker)
                       .queryParam("apikey", properties.getApiKey())
                       .build())
               .retrieve()
               .body(AlphaVantageGlobalQuoteResponse.class);
           validateResponse(response);
           return toPriceDto(response);
       } catch (RestClientException e) {
           throw new MarketDataProviderException("Failed to retrieve data from Alpha Vantage API", e);
       }

    }
    private PriceDto toPriceDto(AlphaVantageGlobalQuoteResponse response) {
        return  new PriceDto(
                response.globalQuote().symbol(),
                response.globalQuote().currentPrice(),
                Currency.USD,
                MarketDataProvider.ALPHA_VANTAGE,
                Instant.now());
    }
    private void validateResponse(AlphaVantageGlobalQuoteResponse response) throws MarketDataProviderException {
        if (response == null) {
            throw new MarketDataProviderException("Received empty response from Alpha Vantage API");
        }
        if (response.information() != null) {
            throw new MarketDataProviderRateLimitException("Alpha Vantage daily request limit exceeded");
        }
        AlphaVantageGlobalQuote quote = response.globalQuote();
        if (quote == null
        || quote.symbol() == null || quote.currentPrice() == null) {
            throw new MarketDataProviderException("Received invalid response from Alpha Vantage");
        }
    }
}
