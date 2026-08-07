package com.investmenttracker.repository;

import com.investmenttracker.entity.Currency;
import com.investmenttracker.entity.Transaction;
import com.investmenttracker.entity.TransactionType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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
}
