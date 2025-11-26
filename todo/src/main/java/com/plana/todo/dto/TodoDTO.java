package com.plana.todo.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.plana.todo.domain.Category;
import com.plana.todo.domain.Priority;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TodoDTO {
    private UUID id;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private OffsetDateTime dueDate;
    private OffsetDateTime reminderDate;
    private boolean reminderSent;
    private boolean completed;
    private OffsetDateTime completedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
