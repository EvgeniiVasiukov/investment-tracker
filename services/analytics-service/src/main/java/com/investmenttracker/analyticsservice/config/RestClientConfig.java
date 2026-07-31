package com.investmenttracker.analyticsservice.config;

import com.investmenttracker.analyticsservice.config.properties.PortfolioServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private final PortfolioServiceProperties portfolioServiceProperties;

    public RestClientConfig(PortfolioServiceProperties portfolioServiceProperties) {
        this.portfolioServiceProperties = portfolioServiceProperties;
    }
    @Bean
    public RestClient portfolioRestClient() {
        return RestClient.builder()
                .baseUrl(portfolioServiceProperties.getBaseUrl())
                .build();
    }
}
