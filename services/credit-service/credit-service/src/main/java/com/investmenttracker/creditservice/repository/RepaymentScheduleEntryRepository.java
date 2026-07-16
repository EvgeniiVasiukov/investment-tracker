package com.investmenttracker.creditservice.repository;

import com.investmenttracker.creditservice.entity.RepaymentScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentScheduleEntryRepository extends JpaRepository<RepaymentScheduleEntry, Long> {
}
