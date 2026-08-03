package com.investmenttracker.analyticsservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.market")
@Getter
@Setter
public class MarketDataServiceProperties {
    String baseUrl;
}
