package com.plana.todo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "todos", indexes = {
        @Index(name = "idx_todo_category", columnList = "category"),
        @Index(name = "idx_todo_priority", columnList = "priority"),
        @Index(name = "idx_todo_due_date", columnList = "due_date"),
        @Index(name = "idx_todo_reminder", columnList = "reminder_date")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category = Category.GENERAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Column(name = "due_date")
    private OffsetDateTime dueDate;

    @Column(name = "reminder_date")
    private OffsetDateTime reminderDate;

    @Column(name = "reminder_sent", nullable = false)
    private boolean reminderSent = false;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}
