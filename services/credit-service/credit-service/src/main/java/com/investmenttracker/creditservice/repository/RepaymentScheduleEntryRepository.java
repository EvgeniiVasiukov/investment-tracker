package com.investmenttracker.creditservice.repository;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntry;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepaymentScheduleEntryRepository extends JpaRepository<RepaymentScheduleEntry, Long> {
public Optional<RepaymentScheduleEntry> findFirstByCreditAndStatusOrderByInstallmentNumberAsc(Credit credit, RepaymentScheduleEntryStatus status);
}
