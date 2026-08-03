package com.investmenttracker.analyticsservice.client;

import com.investmenttracker.analyticsservice.dto.PriceDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MarketClient {
    private final RestClient restClient;

    public MarketClient(@Qualifier("marketRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public PriceDto getPrice(String ticker) {
        return restClient.get()
                .uri("/prices/{ticker}", ticker)
                .retrieve()
                .body(PriceDto.class);
    }
}
