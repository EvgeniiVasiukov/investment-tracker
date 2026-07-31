package com.investmenttracker.analyticsservice.controller;

import com.investmenttracker.analyticsservice.client.PortfolioClient;
import com.investmenttracker.analyticsservice.dto.PortfolioPositionsPageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final PortfolioClient portfolioClient;

    public TestController(PortfolioClient portfolioClient) {
        this.portfolioClient = portfolioClient;
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
}
