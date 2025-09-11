package org.example.civic_govt.util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IssueSpecification {

    public static Specification<Issue> byReporter(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Join<Issue, User> reportersJoin = root.join("reporters");
            return criteriaBuilder.equal(reportersJoin.get("id"), userId);
        };
    }

    public static Specification<Issue> byAssignee(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("assignee").get("id"), userId);
    }

    public static Specification<Issue> withFilters(IssueFilterDTO filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.getDepartmentName() != null && !filters.getDepartmentName().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("department").get("name"), filters.getDepartmentName()));
            }

            if (filters.getZoneName() != null && !filters.getZoneName().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("zone").get("name"), filters.getZoneName()));
            }

            if (filters.getDistrictName() != null && !filters.getDistrictName().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("district").get("name"), filters.getDistrictName()));
            }

            if (filters.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filters.getStatus()));
            }

            if (filters.getPriority() != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), filters.getPriority()));
            }

            if (filters.getUpdatedAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), filters.getUpdatedAfter()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}