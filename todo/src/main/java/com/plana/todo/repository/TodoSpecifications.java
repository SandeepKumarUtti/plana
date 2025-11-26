package com.plana.todo.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.plana.todo.domain.Category;
import com.plana.todo.domain.Priority;
import com.plana.todo.domain.Todo;

import java.time.OffsetDateTime;

public final class TodoSpecifications {
    private TodoSpecifications() {}

    public static Specification<Todo> hasCategory(Category category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Todo> hasPriority(Priority priority) {
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Todo> isCompleted(Boolean completed) {
        return (root, query, cb) -> completed == null ? null : cb.equal(root.get("completed"), completed);
    }

    public static Specification<Todo> dueBefore(OffsetDateTime dt) {
        return (root, query, cb) -> dt == null ? null : cb.lessThanOrEqualTo(root.get("dueDate"), dt);
    }

    public static Specification<Todo> dueAfter(OffsetDateTime dt) {
        return (root, query, cb) -> dt == null ? null : cb.greaterThanOrEqualTo(root.get("dueDate"), dt);
    }

    public static Specification<Todo> textLike(String text) {
        return (root, query, cb) -> {
            if (text == null || text.trim().isEmpty()) return null;
            String p = "%" + text.toLowerCase() + "%";
            Predicate titleLike = cb.like(cb.lower(root.get("title")), p);
            Predicate descLike = cb.like(cb.lower(root.get("description")), p);
            return cb.or(titleLike, descLike);
        };
    }
}

