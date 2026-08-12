package com.investmenttracker.repository;

import com.investmenttracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    @Query("""
            SELECT SUM(t.realizedProfitLoss)
            FROM Transaction t
            WHERE t.userId = :userId
""")
    BigDecimal sumRealizedProfitLoss(@Param("id")Long userId);
}
