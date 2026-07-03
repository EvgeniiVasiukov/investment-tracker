package com.investmenttracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "market-data.alpha-vantage")
public class AlphaVantageProperties {
    private String baseUrl;
    private String apiKey;
}
