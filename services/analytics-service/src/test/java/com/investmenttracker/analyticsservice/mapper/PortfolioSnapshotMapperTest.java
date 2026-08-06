package com.investmenttracker.analyticsservice.mapper;

import com.investmenttracker.analyticsservice.dto.PortfolioPositionDto;
import com.investmenttracker.analyticsservice.dto.PriceDto;
import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortfolioSnapshotMapperTest {
    private final PortfolioSnapshotMapper mapper = new PortfolioSnapshotMapper();
    @Test
    void shouldMapPortfolioPositionAndPriceSnapshot() {
        PortfolioPositionDto position = mock(PortfolioPositionDto.class);
        PriceDto price = mock(PriceDto.class);

        BigDecimal quantity = new BigDecimal(10);
        BigDecimal averagePrice = new BigDecimal(100);
        BigDecimal currentPrice = new BigDecimal(120);
        Currency currency = Currency.getInstance("EUR");

        when(position.ticker()).thenReturn("NVDA");
        when(position.quantity()).thenReturn(quantity);
        when(position.averagePrice()).thenReturn(averagePrice);

        when(price.currentPrice()).thenReturn(currentPrice);
        when(price.currency()).thenReturn(currency);

        PortfolioSnapshotPosition result = mapper.toPortfolioSnapshotPosition(position, price);

        assertEquals("NVDA", result.ticker());
        assertEquals(0,quantity.compareTo(result.quantity()));
        assertEquals(0,averagePrice.compareTo(result.averagePrice()));
        assertEquals(0,currentPrice.compareTo(result.currentPrice()));
        assertEquals(currency,result.currency());
    }
}
