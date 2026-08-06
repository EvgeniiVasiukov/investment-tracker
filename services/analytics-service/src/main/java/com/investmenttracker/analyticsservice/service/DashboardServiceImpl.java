package com.investmenttracker.analyticsservice.service;

import com.investmenttracker.analyticsservice.calculator.*;
import com.investmenttracker.analyticsservice.client.CreditClient;
import com.investmenttracker.analyticsservice.client.MarketClient;
import com.investmenttracker.analyticsservice.client.PortfolioClient;
import com.investmenttracker.analyticsservice.dto.*;
import com.investmenttracker.analyticsservice.mapper.CreditSnapshotMapper;
import com.investmenttracker.analyticsservice.mapper.PortfolioSnapshotMapper;
import com.investmenttracker.analyticsservice.model.CreditSnapshot;
import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Currency;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    //MAPPERS
    private final PortfolioSnapshotMapper portfolioSnapshotMapper;
    private final CreditSnapshotMapper creditSnapshotMapper;
    //CALCULATORS
    private final InvestedAmountCalculator investedAmountCalculator;
    private final NetWorthCalculator netWorthCalculator;
    private final PortfolioValueCalculator portfolioValueCalculator;
    private final ProfitLossCalculator profitLossCalculator;
    private final RemainingCreditCalculator remainingCreditCalculator;
    //CLIENTS
    private final CreditClient creditClient;
    private final PortfolioClient portfolioClient;
    private final MarketClient marketClient;

    public DashboardServiceImpl(PortfolioSnapshotMapper portfolioSnapshotMapper, CreditSnapshotMapper creditSnapshotMapper, InvestedAmountCalculator investedAmountCalculator, NetWorthCalculator netWorthCalculator, PortfolioValueCalculator portfolioValueCalculator, ProfitLossCalculator profitLossCalculator, RemainingCreditCalculator remainingCreditCalculator, CreditClient creditClient, PortfolioClient portfolioClient, MarketClient marketClient) {
        this.portfolioSnapshotMapper = portfolioSnapshotMapper;
        this.creditSnapshotMapper = creditSnapshotMapper;
        this.investedAmountCalculator = investedAmountCalculator;
        this.netWorthCalculator = netWorthCalculator;
        this.portfolioValueCalculator = portfolioValueCalculator;
        this.profitLossCalculator = profitLossCalculator;
        this.remainingCreditCalculator = remainingCreditCalculator;
        this.creditClient = creditClient;
        this.portfolioClient = portfolioClient;
        this.marketClient = marketClient;
    }

    @Override
    public DashboardDto getDashboard(String authToken) {
        PortfolioPositionsPageDto page = portfolioClient.getPortfolioPositions(authToken);
        List<PortfolioSnapshotPosition> portfolioSnapshotPositions = buildPortfolioSnapshot(page.content());
        var totalPortfolioValue = portfolioValueCalculator.calculate(portfolioSnapshotPositions);
        var totalInvestedAmount = investedAmountCalculator.calculate(portfolioSnapshotPositions);
        var unrealizedProfitLoss = profitLossCalculator.calculate(portfolioSnapshotPositions);

        CreditSnapshot creditSnapshot = buildCreditSnapshot(creditClient.getCredit(authToken));
        var remainingCredit = remainingCreditCalculator.calculate(creditSnapshot);

        var netWorth = netWorthCalculator.calculate(totalPortfolioValue, remainingCredit);
        var currency = resolveCurrency(portfolioSnapshotPositions);
        return new DashboardDto(
                new MoneyDto(totalPortfolioValue, currency),
                new MoneyDto(totalInvestedAmount, currency),
                new MoneyDto(unrealizedProfitLoss, currency),
                new MoneyDto(remainingCredit, currency),
                new MoneyDto(netWorth, currency),
                Instant.now());

    }

    private List<PortfolioSnapshotPosition> buildPortfolioSnapshot(List<PortfolioPositionDto> positions) {
        if (positions == null || positions.isEmpty()) {
            return List.of();
        }
        return positions.stream()
                .map(position -> {
                    PriceDto price = marketClient.getPrice(position.ticker());
                    return portfolioSnapshotMapper.toPortfolioSnapshotPosition(position, price);
                }
                )
                .toList();
    }
    private Currency resolveCurrency(List<PortfolioSnapshotPosition> portfolioSnapshot) {
        return portfolioSnapshot.stream()
                .findFirst()
                .map(PortfolioSnapshotPosition::currency)
                .orElse(null);
    }
    private CreditSnapshot buildCreditSnapshot(CreditResponse creditResponse) {
        return creditSnapshotMapper.toCreditSnapshot(creditResponse);
    }

}
