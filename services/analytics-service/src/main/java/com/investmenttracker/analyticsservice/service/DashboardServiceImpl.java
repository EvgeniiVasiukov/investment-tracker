package com.investmenttracker.analyticsservice.service;

import com.investmenttracker.analyticsservice.dto.DashboardDto;
import com.investmenttracker.analyticsservice.dto.MoneyDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
@Service
public class DashboardServiceImpl implements DashboardService {
    Currency eur = Currency.getInstance("EUR");
    MoneyDto portfolioValue = new MoneyDto(new BigDecimal("20000.00"), eur);
    MoneyDto investedAmount = new MoneyDto(new BigDecimal("18500.00"), eur);
    MoneyDto unrealizedProfitLoss = new MoneyDto(new BigDecimal("1500.00"), eur);
    MoneyDto remainingCreditBalance = new MoneyDto(new BigDecimal("12000.00"), eur);
    MoneyDto netWorth = new MoneyDto(new BigDecimal("8000.00"), eur);
    DashboardDto dashboard = new DashboardDto(portfolioValue, investedAmount, unrealizedProfitLoss,remainingCreditBalance,netWorth, Instant.now());

    @Override
    public DashboardDto getDashboard() {
        return dashboard;
    }
}
