package com.investmenttracker.analyticsservice.service;

import com.investmenttracker.analyticsservice.dto.DashboardDto;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    DashboardDto getDashboard();
}
