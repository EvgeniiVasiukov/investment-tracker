package com.investmenttracker.repository;

import com.investmenttracker.dto.response.TransactionSummaryResponse;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.entity.Transaction;
import com.investmenttracker.entity.TransactionType;
import com.investmenttracker.security.SecurityUtils;
import com.investmenttracker.service.TransactionService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    EntityManager entityManager;
    @Test
    void saveAndFindById() {
        Transaction test = transactionRepository.save(Transaction.builder()
                .userId(1l)
                .transactionType(TransactionType.BUY)
                .ticker("TEST")
                .quantity(BigDecimal.TEN)
                .price(BigDecimal.TEN)
                .currency(Currency.USD)
                .fees(BigDecimal.ONE)
                .tax(BigDecimal.ZERO)
                .transactionDate(LocalDateTime.now())
                .build());
        transactionRepository.flush();
        entityManager.clear();
        Optional<Transaction> byId = transactionRepository.findById(test.getId());
        Assertions.assertTrue(byId.isPresent());
        Assertions.assertEquals(test.getId(), byId.get().getId());
        Assertions.assertEquals("TEST", byId.get().getTicker());
        Assertions.assertEquals(TransactionType.BUY, byId.get().getTransactionType());
        Assertions.assertEquals(Currency.USD, byId.get().getCurrency());
    }
    @Test
    void shouldSumRealizedProfitLossForUser() {
        transactionRepository.save(Transaction.builder()
                .userId(1L)
                .transactionType(TransactionType.BUY)
                .ticker("NVDA")
                .quantity(BigDecimal.ONE)
                .price(BigDecimal.valueOf(100))
                .currency(Currency.USD)
                .fees(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .realizedProfitLoss(null)
                .transactionDate(LocalDateTime.now())
                .build());

        transactionRepository.save(Transaction.builder()
                .userId(1L)
                .transactionType(TransactionType.SELL)
                .ticker("NVDA")
                .quantity(BigDecimal.ONE)
                .price(BigDecimal.valueOf(120))
                .currency(Currency.USD)
                .fees(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .realizedProfitLoss(BigDecimal.valueOf(100))
                .transactionDate(LocalDateTime.now())
                .build());

        transactionRepository.save(Transaction.builder()
                .userId(1L)
                .transactionType(TransactionType.SELL)
                .ticker("MSFT")
                .quantity(BigDecimal.ONE)
                .price(BigDecimal.valueOf(90))
                .currency(Currency.USD)
                .fees(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .realizedProfitLoss(BigDecimal.valueOf(-30))
                .transactionDate(LocalDateTime.now())
                .build());

        transactionRepository.save(Transaction.builder()
                .userId(2L)
                .transactionType(TransactionType.SELL)
                .ticker("AAPL")
                .quantity(BigDecimal.ONE)
                .price(BigDecimal.valueOf(150))
                .currency(Currency.USD)
                .fees(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .realizedProfitLoss(BigDecimal.valueOf(999))
                .transactionDate(LocalDateTime.now())
                .build());

        transactionRepository.flush();
        entityManager.clear();

        BigDecimal result = transactionRepository.sumRealizedProfitLoss(1L);

        Assertions.assertEquals(
                0,
                result.compareTo(BigDecimal.valueOf(70))
        );
    }

}
