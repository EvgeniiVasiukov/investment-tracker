package com.investmenttracker.specification;

import com.investmenttracker.dto.request.TransactionFilter;
import com.investmenttracker.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transaction> byFilter(TransactionFilter filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.userId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), filter.userId()));
            }
            if (filter.ticker() != null) {
                predicates.add(criteriaBuilder.equal(root.get("ticker"), filter.ticker()));
            }
            if (filter.transactionType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("transactionType"), filter.transactionType()));
            }
            if (filter.dateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), filter.dateFrom()));
            }
            if (filter.dateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), filter.dateTo()));
            }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

    }
}
