package com.investmenttracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        @NotNull
        @Column(name = "user_id", nullable = false)
        Long userId;
        @Enumerated(EnumType.STRING)
                @Column(name = "transaction_type", nullable = false)
                @NotNull
        TransactionType transactionType;
        @NotBlank
                @Column(name = "ticker", nullable = false)
        String ticker;
        @NotNull
        @Positive
        @Column(name = "quantity", precision = 19, scale = 6, nullable = false)
        BigDecimal quantity;
        @NotNull
        @Positive
        @Column(name = "price", precision = 19, scale = 6, nullable = false)
        BigDecimal price;
        @Enumerated(EnumType.STRING)
        @NotNull
                @Column(name = "currency", nullable = false)
        Currency currency;
        @PositiveOrZero
        @Column(name = "fees", precision = 19, scale = 6, nullable = false)
        BigDecimal fees;
        @PositiveOrZero
        @Column(name = "tax", precision = 19, scale = 6, nullable = false)
        BigDecimal tax;
        @Column(name = "transaction_date", nullable = false)
                @NotNull
        LocalDateTime transactionDate;

}
