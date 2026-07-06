package com.investmenttracker.userservice.specification;

import com.investmenttracker.userservice.dto.UserFilter;
import com.investmenttracker.userservice.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class UserSpecification {
    public static Specification<User> byFilter(UserFilter userFilter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userFilter.email() != null) {
                predicates.add((criteriaBuilder.equal(root.get("email"), userFilter.email())));
            }
            if (userFilter.role() != null) {
                predicates.add((criteriaBuilder.equal(root.get("role"), userFilter.role())));
            }
            if (userFilter.status() != null) {
                predicates.add((criteriaBuilder.equal(root.get("status"), userFilter.status())));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
