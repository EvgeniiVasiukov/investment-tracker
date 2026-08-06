package com.investmenttracker.analyticsservice.mapper;

import com.investmenttracker.analyticsservice.dto.PortfolioPositionDto;
import com.investmenttracker.analyticsservice.dto.PriceDto;
import com.investmenttracker.analyticsservice.model.CreditSnapshot;
import com.investmenttracker.analyticsservice.model.PortfolioSnapshotPosition;
import org.springframework.stereotype.Component;

@Component
public class PortfolioSnapshotMapper {
    public PortfolioSnapshotPosition toPortfolioSnapshotPosition(PortfolioPositionDto position, PriceDto price) {
        return new PortfolioSnapshotPosition(position.ticker(),
                position.quantity(),
                price.currentPrice(),
                position.averagePrice(),
                price.currency());
    }
}
