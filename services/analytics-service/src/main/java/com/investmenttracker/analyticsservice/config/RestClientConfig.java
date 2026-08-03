package com.investmenttracker.analyticsservice.config;

import com.investmenttracker.analyticsservice.config.properties.MarketDataServiceProperties;
import com.investmenttracker.analyticsservice.config.properties.PortfolioServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private final PortfolioServiceProperties portfolioServiceProperties;
    private final MarketDataServiceProperties marketDataServiceProperties;

    public RestClientConfig(PortfolioServiceProperties portfolioServiceProperties,
                            MarketDataServiceProperties marketDataServiceProperties) {
        this.portfolioServiceProperties = portfolioServiceProperties;
        this.marketDataServiceProperties = marketDataServiceProperties;
    }
    @Bean
    public RestClient portfolioRestClient() {
        return RestClient.builder()
                .baseUrl(portfolioServiceProperties.getBaseUrl())
                .build();
    }
    @Bean
    public RestClient marketRestClient() {
        return RestClient.builder()
                .baseUrl(marketDataServiceProperties.getBaseUrl())
                .build();
    }
}
