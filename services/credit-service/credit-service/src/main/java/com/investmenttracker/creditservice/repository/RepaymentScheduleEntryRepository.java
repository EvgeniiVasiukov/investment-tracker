package com.investmenttracker.creditservice.repository;

import com.investmenttracker.creditservice.entity.Credit;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntry;
import com.investmenttracker.creditservice.entity.RepaymentScheduleEntryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepaymentScheduleEntryRepository extends JpaRepository<RepaymentScheduleEntry, Long> {
Optional<RepaymentScheduleEntry> findFirstByCreditAndStatusOrderByInstallmentNumberAsc(Credit credit, RepaymentScheduleEntryStatus status);
List<RepaymentScheduleEntry> findByCreditAndStatusOrderByInstallmentNumberAsc(Credit credit, RepaymentScheduleEntryStatus status);
List<RepaymentScheduleEntry> findAllByCreditOrderByInstallmentNumberAsc(Credit credit);
}
