package com.investmenttracker.analyticsservice.client;

import com.investmenttracker.analyticsservice.dto.CreditResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
@Component
public class CreditClient {
    private final RestClient restClient;
    public CreditClient(@Qualifier("creditRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public CreditResponse getCredit(String authorizationHeader) {
        return restClient.get()
                .uri("/credits/me")
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(CreditResponse.class);
    }
}
