package com.investmenttracker.creditservice.repository;

import com.investmenttracker.creditservice.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
