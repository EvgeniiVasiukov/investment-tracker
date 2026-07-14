package com.investmenttracker.creditservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "repayment_schedule_entries")
@NoArgsConstructor
@Getter
@Setter
public class RepaymentScheduleEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;
    @Enumerated(EnumType.STRING)
    private RepaymenScheduleEntryStatus status;
    @Column(name = "installment_number", nullable = false)
    private Integer instalmentNumber;
    @Column(name="payment_date", nullable = false)
    private LocalDate paymentDate;
    @Column(name = "total_payment_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPaymentAmount;

    @Column(name = "principal_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal interestAmount;

    @Column(name = "remaining_principal_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal remainingPrincipalAmount;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
