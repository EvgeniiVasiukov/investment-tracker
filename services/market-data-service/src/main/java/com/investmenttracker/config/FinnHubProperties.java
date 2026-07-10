package com.investmenttracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "market-data.finnhub")
public class FinnHubProperties {
    private String baseUrl;
    private String apiKey;
}
