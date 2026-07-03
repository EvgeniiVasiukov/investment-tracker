package com.investmenttracker.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userId;
    String ticker;
    BigDecimal quantity;
    BigDecimal averagePrice;
    @Enumerated(EnumType.STRING)
    Currency currency;
    @CreationTimestamp
    LocalDateTime createdAt;

}
