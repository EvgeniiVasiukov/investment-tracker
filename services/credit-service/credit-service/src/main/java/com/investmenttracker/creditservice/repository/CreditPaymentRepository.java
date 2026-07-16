package com.investmenttracker.creditservice.repository;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditPaymentRepository extends JpaRepository<CreditPayment, Long> {
    List<CreditPayment> findByCreditOrderByPaymentDateAsc(Credit credit);
    boolean existsByCredit (Credit credit);
}
