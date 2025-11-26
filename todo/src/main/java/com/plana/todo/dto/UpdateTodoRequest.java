package com.plana.todo.dto;

import lombok.*;

import java.time.OffsetDateTime;

import com.plana.todo.domain.Category;
import com.plana.todo.domain.Priority;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateTodoRequest {
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private OffsetDateTime dueDate;
    private OffsetDateTime reminderDate;
    private Boolean completed;
    private Boolean reminderSent; // allow administrative marking when needed
}
