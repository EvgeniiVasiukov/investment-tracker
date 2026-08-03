package com.investmenttracker.analyticsservice;

import com.investmenttracker.analyticsservice.config.properties.JwtProperties;
import com.investmenttracker.analyticsservice.config.properties.MarketDataServiceProperties;
import com.investmenttracker.analyticsservice.config.properties.PortfolioServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class,
		PortfolioServiceProperties.class,
		MarketDataServiceProperties.class})
public class AnalyticsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsServiceApplication.class, args);
	}

}
