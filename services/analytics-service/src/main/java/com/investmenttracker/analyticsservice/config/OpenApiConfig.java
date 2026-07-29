package com.investmenttracker.analyticsservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI analyticsServerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Investment Tracker - Analytics-service")
                        .description("Provides aggregated analytics and financial insights based on user portfolio")
                        .version("1.0.0"));
    }
}
