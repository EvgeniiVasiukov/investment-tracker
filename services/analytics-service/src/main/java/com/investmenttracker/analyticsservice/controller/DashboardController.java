package com.investmenttracker.analyticsservice.controller;

import com.investmenttracker.analyticsservice.dto.DashboardDto;
import com.investmenttracker.analyticsservice.service.DashboardService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardDto getDashboard(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        return dashboardService.getDashboard(authToken);
    }
}
