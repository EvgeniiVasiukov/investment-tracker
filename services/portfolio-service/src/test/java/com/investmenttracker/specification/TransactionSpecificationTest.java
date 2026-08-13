package com.investmenttracker.specification;

import com.investmenttracker.dto.request.TransactionFilter;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.entity.Transaction;
import com.investmenttracker.entity.TransactionType;
import com.investmenttracker.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TransactionSpecificationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void shouldFilterTransactionsByAllFilters() {
        LocalDateTime transactionDate =
                LocalDateTime.of(2026, 8, 10, 12, 0);

        Transaction matchingTransaction = Transaction.builder()
                .userId(1L)
                .transactionType(TransactionType.SELL)
                .ticker("NVDA")
                .quantity(new BigDecimal("2.000000"))
                .price(new BigDecimal("150.000000"))
                .currency(Currency.USD)
                .fees(BigDecimal.ONE)
                .tax(BigDecimal.ZERO)
                .realizedProfitLoss(new BigDecimal("100.000000"))
                .transactionDate(transactionDate)
                .build();

        Transaction wrongUser = Transaction.builder()
                .userId(2L)
                .transactionType(TransactionType.SELL)
                .ticker("NVDA")
                .quantity(new BigDecimal("2.000000"))
                .price(new BigDecimal("150.000000"))
                .currency(Currency.USD)
                .fees(BigDecimal.ONE)
                .tax(BigDecimal.ZERO)
                .realizedProfitLoss(new BigDecimal("100.000000"))
                .transactionDate(transactionDate)
                .build();

        Transaction wrongTicker = Transaction.builder()
                .userId(1L)
                .transactionType(TransactionType.SELL)
                .ticker("AAPL")
                .quantity(new BigDecimal("2.000000"))
                .price(new BigDecimal("150.000000"))
                .currency(Currency.USD)
                .fees(BigDecimal.ONE)
                .tax(BigDecimal.ZERO)
                .realizedProfitLoss(new BigDecimal("100.000000"))
                .transactionDate(transactionDate)
                .build();

        transactionRepository.save(matchingTransaction);
        transactionRepository.save(wrongUser);
        transactionRepository.save(wrongTicker);

        TransactionFilter filter = new TransactionFilter(
                " nvda ",
                TransactionType.SELL,
                LocalDateTime.of(2026, 8, 1, 0, 0),
                LocalDateTime.of(2026, 8, 31, 23, 59)
        );

        Specification<Transaction> specification =
                TransactionSpecification.byFilter(filter, 1L);

        Page<Transaction> result =
                transactionRepository.findAll(
                        specification,
                        PageRequest.of(0, 20)
                );

        assertEquals(1, result.getTotalElements());
        assertEquals("NVDA", result.getContent().getFirst().getTicker());
        assertEquals(1L, result.getContent().getFirst().getUserId());
        assertEquals(
                TransactionType.SELL,
                result.getContent().getFirst().getTransactionType()
        );
    }
}
