package com.investmenttracker.analyticsservice.controller;

import com.investmenttracker.analyticsservice.dto.DashboardDto;
import com.investmenttracker.analyticsservice.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardDto getDashboard() {
        return dashboardService.getDashboard();
    }
}
