package com.plana.todo.dto;

import lombok.*;

import java.time.OffsetDateTime;

import com.plana.todo.domain.Category;
import com.plana.todo.domain.Priority;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTodoRequest {
    @NotBlank
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private OffsetDateTime dueDate;
    private OffsetDateTime reminderDate;
}
