package com.investmenttracker.repository;

import com.investmenttracker.entity.Position;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface PositionRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {
    public Optional<Position> findByTickerAndUserId(String ticker, Long userId);

}
