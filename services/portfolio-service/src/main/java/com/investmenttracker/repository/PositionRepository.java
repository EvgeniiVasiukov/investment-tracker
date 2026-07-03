package com.investmenttracker.repository;

import com.investmenttracker.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface PositionRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {

}
