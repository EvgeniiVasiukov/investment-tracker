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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTest {
    private static final String AUTHORIZATION_HEADER = "Bearer test-token";
    public static final Currency USD = Currency.getInstance("USD");
    @Mock
    private PortfolioClient portfolioClient;
    @Mock
    private CreditClient creditClient;
    @Mock
    private MarketClient marketClient;
    @Mock
    private PortfolioSnapshotMapper portfolioSnapshotMapper;
    @Mock
    private CreditSnapshotMapper creditSnapshotMapper;
    @Mock
    private PortfolioValueCalculator portfolioValueCalculator;
    @Mock
    private InvestedAmountCalculator investedAmountCalculator;
    @Mock
    private ProfitLossCalculator profitLossCalculator;
    @Mock
    private RemainingCreditCalculator remainingCreditCalculator;
    @Mock
    private NetWorthCalculator netWorthCalculator;
    @Mock
    private TotalProfitLossCalculator totalProfitLossCalculator;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void shouldBuildDashboard() {
        PortfolioPositionDto position = org.mockito.Mockito.mock(
                PortfolioPositionDto.class
        );
        PortfolioPositionsPageDto page = org.mockito.Mockito.mock(
                PortfolioPositionsPageDto.class
        );
        PriceDto price = org.mockito.Mockito.mock(PriceDto.class);
        CreditResponse creditResponse = org.mockito.Mockito.mock(
                CreditResponse.class
        );

        TransactionSummaryDto transactionSummary =
                new TransactionSummaryDto(new BigDecimal("25"));

        PortfolioSnapshotPosition portfolioSnapshot =
                new PortfolioSnapshotPosition(
                        "NVDA",
                        new BigDecimal("2"),
                        new BigDecimal("120"),
                        new BigDecimal("100"),
                        USD
                );

        CreditSnapshot creditSnapshot =
                new CreditSnapshot(
                        new BigDecimal("3000"),
                        CreditStatus.ACTIVE
                );

        List<PortfolioPositionDto> positions = List.of(position);
        List<PortfolioSnapshotPosition> snapshots =
                List.of(portfolioSnapshot);

        when(page.content()).thenReturn(positions);
        when(position.ticker()).thenReturn("NVDA");

        when(portfolioClient.getPortfolioPositions(AUTHORIZATION_HEADER))
                .thenReturn(page);

        when(marketClient.getPrice("NVDA"))
                .thenReturn(price);

        when(portfolioSnapshotMapper.toPortfolioSnapshotPosition(
                position,
                price
        )).thenReturn(portfolioSnapshot);

        when(portfolioValueCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("240"));

        when(investedAmountCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("200"));

        when(profitLossCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("40"));

        when(portfolioClient.getTransactionSummary(AUTHORIZATION_HEADER))
                .thenReturn(transactionSummary);

        when(totalProfitLossCalculator.calculate(
                new BigDecimal("40"),
                new BigDecimal("25")
        )).thenReturn(new BigDecimal("65"));

        when(creditClient.getCredit(AUTHORIZATION_HEADER))
                .thenReturn(creditResponse);

        when(creditSnapshotMapper.toCreditSnapshot(creditResponse))
                .thenReturn(creditSnapshot);

        when(remainingCreditCalculator.calculate(creditSnapshot))
                .thenReturn(new BigDecimal("3000"));

        when(netWorthCalculator.calculate(
                new BigDecimal("240"),
                new BigDecimal("3000")
        )).thenReturn(new BigDecimal("-2760"));

        DashboardDto result =
                dashboardService.getDashboard(AUTHORIZATION_HEADER);

        assertNotNull(result);

        assertBigDecimalEquals(
                "240",
                result.totalPortfolioValue().amount()
        );

        assertEquals(
                USD,
                result.totalPortfolioValue().currency()
        );

        assertBigDecimalEquals(
                "200",
                result.totalInvestedAmount().amount()
        );

        assertBigDecimalEquals(
                "40",
                result.unrealizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "3000",
                result.remainingCreditBalance().amount()
        );

        assertBigDecimalEquals(
                "-2760",
                result.netWorth().amount()
        );

        assertBigDecimalEquals(
                "25",
                result.realizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "65",
                result.totalProfitLoss().amount()
        );

        assertNotNull(result.calculatedAt());

        verify(portfolioClient)
                .getPortfolioPositions(AUTHORIZATION_HEADER);

        verify(portfolioClient)
                .getTransactionSummary(AUTHORIZATION_HEADER);

        verify(marketClient)
                .getPrice("NVDA");

        verify(creditClient)
                .getCredit(AUTHORIZATION_HEADER);
    }
    @Test
    void shouldBuildDashboardForEmptyPortfolio() {
        PortfolioPositionsPageDto page = org.mockito.Mockito.mock(
                PortfolioPositionsPageDto.class
        );
        CreditResponse creditResponse = org.mockito.Mockito.mock(
                CreditResponse.class
        );

        TransactionSummaryDto transactionSummary =
                new TransactionSummaryDto(BigDecimal.ZERO);

        CreditSnapshot creditSnapshot =
                new CreditSnapshot(
                        new BigDecimal("3000"),
                        CreditStatus.ACTIVE
                );

        List<PortfolioSnapshotPosition> emptySnapshot = List.of();

        when(page.content()).thenReturn(List.of());

        when(portfolioClient.getPortfolioPositions(AUTHORIZATION_HEADER))
                .thenReturn(page);

        when(portfolioValueCalculator.calculate(emptySnapshot))
                .thenReturn(BigDecimal.ZERO);

        when(investedAmountCalculator.calculate(emptySnapshot))
                .thenReturn(BigDecimal.ZERO);

        when(profitLossCalculator.calculate(emptySnapshot))
                .thenReturn(BigDecimal.ZERO);

        when(portfolioClient.getTransactionSummary(AUTHORIZATION_HEADER))
                .thenReturn(transactionSummary);

        when(totalProfitLossCalculator.calculate(
                BigDecimal.ZERO,
                BigDecimal.ZERO
        )).thenReturn(BigDecimal.ZERO);

        when(creditClient.getCredit(AUTHORIZATION_HEADER))
                .thenReturn(creditResponse);

        when(creditSnapshotMapper.toCreditSnapshot(creditResponse))
                .thenReturn(creditSnapshot);

        when(remainingCreditCalculator.calculate(creditSnapshot))
                .thenReturn(new BigDecimal("3000"));

        when(netWorthCalculator.calculate(
                BigDecimal.ZERO,
                new BigDecimal("3000")
        )).thenReturn(new BigDecimal("-3000"));

        DashboardDto result =
                dashboardService.getDashboard(AUTHORIZATION_HEADER);

        assertBigDecimalEquals(
                "0",
                result.totalPortfolioValue().amount()
        );

        assertBigDecimalEquals(
                "0",
                result.totalInvestedAmount().amount()
        );

        assertBigDecimalEquals(
                "0",
                result.unrealizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "0",
                result.realizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "0",
                result.totalProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "3000",
                result.remainingCreditBalance().amount()
        );

        assertBigDecimalEquals(
                "-3000",
                result.netWorth().amount()
        );

        assertNull(result.totalPortfolioValue().currency());
        assertNull(result.totalInvestedAmount().currency());
        assertNull(result.unrealizedProfitLoss().currency());
        assertNull(result.realizedProfitLoss().currency());
        assertNull(result.totalProfitLoss().currency());
        assertNull(result.remainingCreditBalance().currency());
        assertNull(result.netWorth().currency());

        verify(portfolioClient)
                .getTransactionSummary(AUTHORIZATION_HEADER);

        verify(marketClient, never()).getPrice(
                org.mockito.ArgumentMatchers.anyString()
        );
    }

    @Test
    void shouldBuildDashboardWhenUserHasNoCredit() {
        PortfolioPositionDto position = org.mockito.Mockito.mock(
                PortfolioPositionDto.class
        );
        PortfolioPositionsPageDto page = org.mockito.Mockito.mock(
                PortfolioPositionsPageDto.class
        );
        PriceDto price = org.mockito.Mockito.mock(PriceDto.class);

        TransactionSummaryDto transactionSummary =
                new TransactionSummaryDto(new BigDecimal("50"));

        PortfolioSnapshotPosition portfolioSnapshot =
                new PortfolioSnapshotPosition(
                        "MSFT",
                        new BigDecimal("2"),
                        new BigDecimal("500"),
                        new BigDecimal("400"),
                        USD
                );

        List<PortfolioSnapshotPosition> snapshots =
                List.of(portfolioSnapshot);

        when(page.content()).thenReturn(List.of(position));
        when(position.ticker()).thenReturn("MSFT");

        when(portfolioClient.getPortfolioPositions(AUTHORIZATION_HEADER))
                .thenReturn(page);

        when(marketClient.getPrice("MSFT"))
                .thenReturn(price);

        when(portfolioSnapshotMapper.toPortfolioSnapshotPosition(
                position,
                price
        )).thenReturn(portfolioSnapshot);

        when(portfolioValueCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("1000"));

        when(investedAmountCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("800"));

        when(profitLossCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("200"));

        when(portfolioClient.getTransactionSummary(AUTHORIZATION_HEADER))
                .thenReturn(transactionSummary);

        when(totalProfitLossCalculator.calculate(
                new BigDecimal("200"),
                new BigDecimal("50")
        )).thenReturn(new BigDecimal("250"));

        when(creditClient.getCredit(AUTHORIZATION_HEADER))
                .thenReturn(null);

        when(creditSnapshotMapper.toCreditSnapshot(null))
                .thenReturn(null);

        when(remainingCreditCalculator.calculate(null))
                .thenReturn(BigDecimal.ZERO);

        when(netWorthCalculator.calculate(
                new BigDecimal("1000"),
                BigDecimal.ZERO
        )).thenReturn(new BigDecimal("1000"));

        DashboardDto result =
                dashboardService.getDashboard(AUTHORIZATION_HEADER);

        assertBigDecimalEquals(
                "1000",
                result.totalPortfolioValue().amount()
        );

        assertBigDecimalEquals(
                "800",
                result.totalInvestedAmount().amount()
        );

        assertBigDecimalEquals(
                "200",
                result.unrealizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "50",
                result.realizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "250",
                result.totalProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "0",
                result.remainingCreditBalance().amount()
        );

        assertBigDecimalEquals(
                "1000",
                result.netWorth().amount()
        );

        assertEquals(
                USD,
                result.netWorth().currency()
        );

        verify(portfolioClient)
                .getTransactionSummary(AUTHORIZATION_HEADER);

        verify(creditClient)
                .getCredit(AUTHORIZATION_HEADER);

        verify(creditSnapshotMapper)
                .toCreditSnapshot(null);

        verify(remainingCreditCalculator)
                .calculate(null);
    }
    @Test
    void shouldBuildDashboardWithZeroRealizedProfitLoss() {
        PortfolioPositionDto position = org.mockito.Mockito.mock(
                PortfolioPositionDto.class
        );
        PortfolioPositionsPageDto page = org.mockito.Mockito.mock(
                PortfolioPositionsPageDto.class
        );
        PriceDto price = org.mockito.Mockito.mock(PriceDto.class);
        CreditResponse creditResponse = org.mockito.Mockito.mock(
                CreditResponse.class
        );

        TransactionSummaryDto transactionSummary =
                new TransactionSummaryDto(BigDecimal.ZERO);

        PortfolioSnapshotPosition portfolioSnapshot =
                new PortfolioSnapshotPosition(
                        "NVDA",
                        new BigDecimal("2"),
                        new BigDecimal("120"),
                        new BigDecimal("100"),
                        USD
                );

        CreditSnapshot creditSnapshot =
                new CreditSnapshot(
                        new BigDecimal("3000"),
                        CreditStatus.ACTIVE
                );

        List<PortfolioSnapshotPosition> snapshots =
                List.of(portfolioSnapshot);

        when(page.content())
                .thenReturn(List.of(position));

        when(position.ticker())
                .thenReturn("NVDA");

        when(portfolioClient.getPortfolioPositions(AUTHORIZATION_HEADER))
                .thenReturn(page);

        when(marketClient.getPrice("NVDA"))
                .thenReturn(price);

        when(portfolioSnapshotMapper.toPortfolioSnapshotPosition(
                position,
                price
        )).thenReturn(portfolioSnapshot);

        when(portfolioValueCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("240"));

        when(investedAmountCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("200"));

        when(profitLossCalculator.calculate(snapshots))
                .thenReturn(new BigDecimal("40"));

        when(portfolioClient.getTransactionSummary(AUTHORIZATION_HEADER))
                .thenReturn(transactionSummary);

        when(totalProfitLossCalculator.calculate(
                new BigDecimal("40"),
                BigDecimal.ZERO
        )).thenReturn(new BigDecimal("40"));

        when(creditClient.getCredit(AUTHORIZATION_HEADER))
                .thenReturn(creditResponse);

        when(creditSnapshotMapper.toCreditSnapshot(creditResponse))
                .thenReturn(creditSnapshot);

        when(remainingCreditCalculator.calculate(creditSnapshot))
                .thenReturn(new BigDecimal("3000"));

        when(netWorthCalculator.calculate(
                new BigDecimal("240"),
                new BigDecimal("3000")
        )).thenReturn(new BigDecimal("-2760"));

        DashboardDto result =
                dashboardService.getDashboard(AUTHORIZATION_HEADER);

        assertBigDecimalEquals(
                "40",
                result.unrealizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "0",
                result.realizedProfitLoss().amount()
        );

        assertBigDecimalEquals(
                "40",
                result.totalProfitLoss().amount()
        );

        verify(portfolioClient)
                .getPortfolioPositions(AUTHORIZATION_HEADER);

        verify(portfolioClient)
                .getTransactionSummary(AUTHORIZATION_HEADER);

        verify(creditClient)
                .getCredit(AUTHORIZATION_HEADER);
    }

    private void assertBigDecimalEquals(
            String expected,
            BigDecimal actual
    ) {
        assertEquals(
                0,
                new BigDecimal(expected).compareTo(actual)
        );
    }
}
