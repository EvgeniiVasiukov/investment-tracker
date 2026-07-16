package com.investmenttracker.creditservice.service;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntry;
import com.investmenttracker.creditservice.repository.RepaymentScheduleEntryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RepaymentScheduleServiceTest {
    @Mock
    private RepaymentScheduleEntryRepository repaymentScheduleEntryRepository;
    @InjectMocks
    private RepaymentScheduleService repaymentScheduleService;

    @Test
    void shouldGenerateRepaymentSchedule() {
        Credit credit = new Credit();
        credit.setId(1L);
        credit.setPrincipalAmount(BigDecimal.valueOf(3000));
        credit.setAnnualInterestRate(BigDecimal.valueOf(12));
        credit.setMonthlyPayment(BigDecimal.valueOf(1060));
        credit.setTermMonths(3);
        credit.setStartDate(LocalDate.of(2026, 7, 15));


        when(repaymentScheduleEntryRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        List<RepaymentScheduleEntry> result = repaymentScheduleService.generateSchedule(credit);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        RepaymentScheduleEntry firstEntry = result.get(0);
        Assertions.assertEquals(1, firstEntry.getInstallmentNumber());
        Assertions.assertEquals(LocalDate.of(2026, 7, 30), firstEntry.getPaymentDate());
        Assertions.assertEquals(BigDecimal.valueOf(30.00).setScale(2), firstEntry.getInterestAmount());
        Assertions.assertEquals(BigDecimal.valueOf(1030.00).setScale(2), firstEntry.getPrincipalAmount());
        Assertions.assertEquals(BigDecimal.valueOf(1970.00).setScale(2), firstEntry.getRemainingPrincipalAmount());
        Assertions.assertEquals(0, BigDecimal.valueOf(1060.00).compareTo(firstEntry.getTotalPaymentAmount()));
        verify(repaymentScheduleEntryRepository).saveAll(anyList());
    }
}
