package com.yusuke.practicerepositorytest.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.yusuke.practicerepositorytest.entity.User;

import jakarta.persistence.criteria.Predicate;

public class UserSpecification {

    /**
     * 名前で検索するSpecification
     */
    public static Specification<User> nameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * メールアドレスで検索するSpecification
     */
    public static Specification<User> emailLike(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%");
        };
    }

    /**
     * 年齢範囲で検索するSpecification
     */
    public static Specification<User> ageBetween(Integer minAge, Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            if (minAge == null && maxAge == null) {
                return criteriaBuilder.conjunction();
            }

            if (minAge != null && maxAge != null) {
                return criteriaBuilder.between(root.get("age"), minAge, maxAge);
            } else if (minAge != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge);
            }
        };
    }

    /**
     * 最小年齢で検索するSpecification
     */
    public static Specification<User> ageGreaterThanOrEqualTo(Integer minAge) {
        return (root, query, criteriaBuilder) -> {
            if (minAge == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge);
        };
    }

    /**
     * 最大年齢で検索するSpecification
     */
    public static Specification<User> ageLessThanOrEqualTo(Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            if (maxAge == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge);
        };
    }

    /**
     * 年齢が指定値より大きいSpecification
     */
    public static Specification<User> ageGreaterThan(Integer age) {
        return (root, query, criteriaBuilder) -> {
            if (age == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("age"), age);
        };
    }

    /**
     * 年齢が指定値と等しいSpecification
     */
    public static Specification<User> ageEquals(Integer age) {
        return (root, query, criteriaBuilder) -> {
            if (age == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("age"), age);
        };
    }

    /**
     * メールアドレスが指定値と等しいSpecification
     */
    public static Specification<User> emailEquals(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    /**
     * 複数の条件を組み合わせるSpecification
     */
    public static Specification<User> searchUsers(String name, String email, Integer minAge, Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 名前の条件
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            // メールアドレスの条件
            if (email != null && !email.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"));
            }

            // 年齢範囲の条件
            if (minAge != null && maxAge != null) {
                predicates.add(criteriaBuilder.between(root.get("age"), minAge, maxAge));
            } else if (minAge != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge));
            } else if (maxAge != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}