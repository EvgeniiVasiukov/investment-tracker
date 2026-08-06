package com.investmenttracker.analyticsservice.mapper;

import com.investmenttracker.analyticsservice.dto.CreditResponse;
import com.investmenttracker.analyticsservice.dto.CreditStatus;
import com.investmenttracker.analyticsservice.model.CreditSnapshot;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreditSnapshotMapperTest {
    private CreditSnapshotMapper mapper = new CreditSnapshotMapper();

    @Test
    void sholdMapCreditResponseToCreditSnapshot() {
        CreditResponse creditResponse = mock(CreditResponse.class);
        BigDecimal principalAmount = BigDecimal.valueOf(100);
        when(creditResponse.principalAmount()).thenReturn(principalAmount);
        when(creditResponse.status()).thenReturn(CreditStatus.ACTIVE);

        CreditSnapshot result = mapper.toCreditSnapshot(creditResponse);

        assertEquals(0, principalAmount.compareTo(result.remainingPrincipalAmount()));
        assertEquals(CreditStatus.ACTIVE,result.status());
    }
    @Test
    void shouldReturnNullWhenCreditResponseIsNull() {
        CreditSnapshot result = mapper.toCreditSnapshot(null);
        assertNull(result);
    }
}
