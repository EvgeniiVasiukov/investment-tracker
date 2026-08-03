package com.investmenttracker.analyticsservice.controller;

import com.investmenttracker.analyticsservice.client.MarketClient;
import com.investmenttracker.analyticsservice.client.PortfolioClient;
import com.investmenttracker.analyticsservice.dto.PortfolioPositionsPageDto;
import com.investmenttracker.analyticsservice.dto.PriceDto;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    private final PortfolioClient portfolioClient;
    private final MarketClient marketClient;

    public TestController(PortfolioClient portfolioClient, MarketClient marketClient) {
        this.portfolioClient = portfolioClient;
        this.marketClient = marketClient;
    }

    @GetMapping("/test")
    public String test() {
        return "Analytics service is running";
    }
    @GetMapping("/secure-test")
    public String secureTest() {
        return "Secure test";
    }
    @GetMapping("/portfolio/test")
    public PortfolioPositionsPageDto portfolioTest(@RequestHeader("Authorization") String authorizationHeader) {
        return portfolioClient.getPortfolioPositions(authorizationHeader);
    }
    @GetMapping("/market/test")
    public PriceDto marketTest(@RequestParam("ticker") String ticker) {
        return marketClient.getPrice(ticker);
    }
}
