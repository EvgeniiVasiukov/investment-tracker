package com.investmenttracker.specification;

import com.investmenttracker.dto.PositionFilter;
import com.investmenttracker.entity.Position;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PositionSpecification {
    public static Specification<Position> byFilter(PositionFilter filter){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.ticker() != null) {
                predicates.add((criteriaBuilder.equal(root.get("ticker"), filter.ticker())));
            }
            if (filter.currency() != null) {
                predicates.add((criteriaBuilder.equal(root.get("currency"), filter.currency())));
            }
            if (filter.minAveragePrice() != null) {
                predicates.add((criteriaBuilder.greaterThanOrEqualTo(root.get("averagePrice"), filter.minAveragePrice())));
            }
            if (filter.maxAveragePrice() != null) {
                predicates.add((criteriaBuilder.lessThanOrEqualTo(root.get("averagePrice"), filter.maxAveragePrice())));
            }
            if (filter.userId() != null) {
                predicates.add((criteriaBuilder.equal(root.get("userId"), filter.userId())));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
