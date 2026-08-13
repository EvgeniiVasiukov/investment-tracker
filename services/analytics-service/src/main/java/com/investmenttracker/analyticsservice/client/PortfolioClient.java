package com.investmenttracker.analyticsservice.client;

import com.investmenttracker.analyticsservice.dto.PortfolioPositionsPageDto;
import com.investmenttracker.analyticsservice.dto.TransactionPageDto;
import com.investmenttracker.analyticsservice.dto.TransactionSummaryDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PortfolioClient {
    private final RestClient restClient;

    public PortfolioClient(@Qualifier("portfolioRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public PortfolioPositionsPageDto getPortfolioPositions(String authorizationHeader) {
       return restClient.get()
                .uri("/positions")
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(PortfolioPositionsPageDto.class);
    }
    public TransactionPageDto getTransactions(String authorizationHeader) {
        return restClient.get()
                .uri("/transactions")
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(TransactionPageDto.class);
    }
    public TransactionSummaryDto getTransactionSummary(String authorizationHeader) {
        return restClient.get()
                .uri("/transactions/summary")
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(TransactionSummaryDto.class);
    }

}
