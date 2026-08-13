package com.investmenttracker.specification;

import com.investmenttracker.dto.request.TransactionFilter;
import com.investmenttracker.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionSpecification {
    public static Specification<Transaction> byFilter(TransactionFilter filter, Long userId) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            if (filter.ticker() != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.upper(root.get("ticker")),
                        filter.ticker().trim().toUpperCase(Locale.ROOT)));
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
