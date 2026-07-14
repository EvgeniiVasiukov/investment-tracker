package com.investmenttracker.creditservice.repository;

import com.investmenttracker.creditservice.entity.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditPaymentReoisitory extends JpaRepository<CreditPayment, Long> {
    List<CreditPayment> findByCreditIdOrderByPaymentDateAsc(Long creditId);
}
