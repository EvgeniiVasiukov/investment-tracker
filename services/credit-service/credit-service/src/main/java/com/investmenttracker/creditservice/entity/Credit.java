package com.investmenttracker.creditservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "credits")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CreditStatus status;
    @Column(name = "bank_name", nullable = false, length = 255)
    private String bankName;
    @Column(name = "principal_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;
    @Column(name="annual_interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal annualInterestRate;
    @Column(name="term_months", nullable = false)
    private Integer termMonths;
    @Column(name = "monthly_payment", nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyPayment;
    @OneToMany(mappedBy = "credit",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    orphanRemoval = true)
    private List<CreditPayment> payments;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
