package com.investmenttracker.analyticsservice.mapper;

import com.investmenttracker.analyticsservice.dto.CreditResponse;
import com.investmenttracker.analyticsservice.model.CreditSnapshot;
import org.springframework.stereotype.Component;

@Component
public class CreditSnapshotMapper {
    public CreditSnapshot toCreditSnapshot(CreditResponse response) {
        if (response == null) {
            return null;
        }
        return new CreditSnapshot(response.principalAmount(), response.status());
    }
}
