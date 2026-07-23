package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.exception.CreditNotFoundException;
import com.investmenttracker.creditservice.repository.CreditRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {
    @Mock
    private CreditRepository creditRepository;

    @Mock
    private RepaymentScheduleService repaymentScheduleService;

    @InjectMocks
    private CreditService creditService;

    @Test
    public void creditShouldBeCreated() {
        Credit credit = new Credit();
        credit.setPrincipalAmount(new BigDecimal("3000.00"));
        credit.setAnnualInterestRate(new BigDecimal("12"));
        credit.setMonthlyPayment(new BigDecimal("1060.00"));
        credit.setTermMonths(3);
        credit.setStartDate(LocalDate.of(2026,1,1));
        credit.setBankName("Sparkasse");

        when(creditRepository.save(credit)).thenReturn(credit);

        Credit result = creditService.createCredit(credit);
        Assertions.assertNotNull(result);
        verify(creditRepository).save(credit);
        verify(repaymentScheduleService).generateSchedule(credit);
    }

    @Test
    public void shouldDeleteCreditWithRelatedSchedule() {
        Credit credit = new Credit();
        credit.setId(1L);

        when(creditRepository.findByUserId(1L)).thenReturn(Optional.of(credit));
        creditService.deleteCredit(1L);
        verify(creditRepository).delete(credit);
    }
    @Test
    void shouldThrowExceptionWhenCreditNotFound() {
        when(creditRepository.findByUserId(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(CreditNotFoundException.class, () -> creditService.getCurrentUserCredit(1L));
    }

}
