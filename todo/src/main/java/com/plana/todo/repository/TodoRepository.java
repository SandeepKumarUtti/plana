package com.plana.todo.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.plana.todo.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, UUID>, JpaSpecificationExecutor<Todo> {
    // convenience query for scheduler
    List<Todo> findByReminderDateBetweenAndReminderSentFalseAndCompletedFalse(OffsetDateTime from, OffsetDateTime to);
}
