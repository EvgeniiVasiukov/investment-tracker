package com.investmenttracker.creditservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "repayment_schedule_entries")
@NoArgsConstructor
@Getter
@Setter
public class RepaymentScheduleEntry {
    @Id

}
