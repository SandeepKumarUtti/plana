package com.plana.todo.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.plana.todo.domain.Category;
import com.plana.todo.domain.Priority;
import com.plana.todo.domain.Todo;
import com.plana.todo.dto.CreateTodoRequest;
import com.plana.todo.dto.TodoDTO;
import com.plana.todo.dto.UpdateTodoRequest;
import com.plana.todo.scheduler.ReminderScheduler;
import com.plana.todo.service.TodoService;

import jakarta.validation.Valid;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService svc;
    private final ReminderScheduler reminderScheduler;

    public TodoController(TodoService svc, ReminderScheduler reminderScheduler) {
        this.svc = svc;
        this.reminderScheduler = reminderScheduler;
    }

    @PostMapping
    public ResponseEntity<TodoDTO> create(@Valid @RequestBody CreateTodoRequest req) {
        Todo t = Todo.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .category(Optional.ofNullable(req.getCategory()).orElse(Category.GENERAL))
                .priority(Optional.ofNullable(req.getPriority()).orElse(Priority.MEDIUM))
                .dueDate(req.getDueDate())
                .reminderDate(req.getReminderDate())
                .build();
        Todo saved = svc.create(t);
        return ResponseEntity.created(URI.create("/api/todos/" + saved.getId())).body(toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getOne(@PathVariable UUID id) {
        return svc.findById(id).map(this::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TodoDTO> patch(@PathVariable UUID id, @RequestBody UpdateTodoRequest req) {
        Todo patch = new Todo();
        patch.setTitle(req.getTitle());
        patch.setDescription(req.getDescription());
        patch.setCategory(req.getCategory());
        patch.setPriority(req.getPriority());
        patch.setDueDate(req.getDueDate());
        patch.setReminderDate(req.getReminderDate());
        if (req.getCompleted() != null) {
            patch.setCompleted(req.getCompleted());
            if (req.getCompleted()) patch.setCompletedAt(OffsetDateTime.now());
        }
        if (req.getReminderSent() != null) {
            patch.setReminderSent(req.getReminderSent());
        }
        Todo updated = svc.update(id, patch);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam Optional<String> q,
            @RequestParam Optional<String> category,
            @RequestParam Optional<String> priority,
            @RequestParam Optional<Boolean> completed,
            @RequestParam Optional<OffsetDateTime> dueBefore,
            @RequestParam Optional<OffsetDateTime> dueAfter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam Optional<String> sort
    ) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort.isPresent()) {
            String[] parts = sort.get().split(",");
            String prop = parts[0];
            Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            orders.add(new Sort.Order(dir, prop));
        } else {
            orders.add(new Sort.Order(Sort.Direction.ASC, "dueDate"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "priority"));
        }

        var pageRes = svc.findAll(q, category, priority, completed, dueBefore, dueAfter, page, size, orders);

        Map<String, Object> body = new HashMap<>();
        body.put("content", pageRes.getContent().stream().map(this::toDto).collect(Collectors.toList()));
        body.put("page", pageRes.getNumber());
        body.put("size", pageRes.getSize());
        body.put("totalElements", pageRes.getTotalElements());
        body.put("totalPages", pageRes.getTotalPages());
        
        return ResponseEntity.ok(body);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(svc.stats());
    }

    @PostMapping("/internal/run-reminders")
    public ResponseEntity<Map<String, String>> runReminders() {
        reminderScheduler.scanAndSendReminders();
        return ResponseEntity.ok(Map.of("message", "Reminder scan completed"));
    }

    private TodoDTO toDto(Todo t) {
        return TodoDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .category(t.getCategory())
                .priority(t.getPriority())
                .dueDate(t.getDueDate())
                .reminderDate(t.getReminderDate())
                .reminderSent(t.isReminderSent())
                .completed(t.isCompleted())
                .completedAt(t.getCompletedAt())
                .createdAt(t.getCreatedAt() == null ? null : t.getCreatedAt().atOffset(OffsetDateTime.now().getOffset()))
                .updatedAt(t.getUpdatedAt() == null ? null : t.getUpdatedAt().atOffset(OffsetDateTime.now().getOffset()))
                .build();
    }
}

