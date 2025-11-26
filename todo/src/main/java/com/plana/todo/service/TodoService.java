package com.plana.todo.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plana.todo.domain.Category;
import com.plana.todo.domain.Priority;
import com.plana.todo.domain.Todo;
import com.plana.todo.repository.TodoRepository;
import com.plana.todo.repository.TodoSpecifications;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@Transactional
public class TodoService {
    private final TodoRepository repo;
    private final NotificationService notificationService;

    public TodoService(TodoRepository repo, NotificationService notificationService) {
        this.repo = repo;
        this.notificationService = notificationService;
    }

    public Todo create(Todo t) {
        if (t.isCompleted() && t.getCompletedAt() == null) {
            t.setCompletedAt(OffsetDateTime.now());
        }
        return repo.save(t);
    }

    public Todo update(UUID id, Todo patch) {
        Todo existing = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Todo not found: " + id));
        if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
        if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
        if (patch.getCategory() != null) existing.setCategory(patch.getCategory());
        if (patch.getPriority() != null) existing.setPriority(patch.getPriority());
        if (patch.getDueDate() != null) existing.setDueDate(patch.getDueDate());
        if (patch.getReminderDate() != null) existing.setReminderDate(patch.getReminderDate());

        if (patch.isCompleted() && !existing.isCompleted()) {
            existing.setCompleted(true);
            existing.setCompletedAt(OffsetDateTime.now());
        } else if (!patch.isCompleted() && existing.isCompleted()) {
            existing.setCompleted(false);
            existing.setCompletedAt(null);
        }
        if (patch.isReminderSent()) {
            existing.setReminderSent(true);
        }
        return repo.save(existing);
    }

    public Optional<Todo> findById(UUID id) {
        return repo.findById(id);
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }

    public Page<Todo> findAll(
            Optional<String> q,
            Optional<String> category,
            Optional<String> priority,
            Optional<Boolean> completed,
            Optional<OffsetDateTime> dueBefore,
            Optional<OffsetDateTime> dueAfter,
            int page, int size, List<Sort.Order> sortOrders
    ) {
        Specification<Todo> spec = Specification.where(null);

        spec = spec.and(TodoSpecifications.textLike(q.orElse(null)));
        spec = spec.and(TodoSpecifications.isCompleted(completed.orElse(null)));
        spec = spec.and(TodoSpecifications.dueBefore(dueBefore.orElse(null)));
        spec = spec.and(TodoSpecifications.dueAfter(dueAfter.orElse(null)));

        if (category.isPresent()) {
            try {
                spec = spec.and(TodoSpecifications.hasCategory(Category.valueOf(category.get().toUpperCase())));
            } catch (IllegalArgumentException ex) {
                // ignore
            }
        }
        if (priority.isPresent()) {
            try {
                spec = spec.and(TodoSpecifications.hasPriority(Priority.valueOf(priority.get().toUpperCase())));
            } catch (IllegalArgumentException ex) {
                // ignore
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrders));
        return repo.findAll(spec, pageable);
    }

    public Map<String, Object> stats() {
        long total = repo.count();
        long completed = repo.count(Specification.where(TodoSpecifications.isCompleted(true)));
        long pending = total - completed;
        double completionRate = total == 0 ? 0.0 : ((double) completed / total) * 100.0;

        Map<String, Object> m = new HashMap<>();
        m.put("total", total);
        m.put("completed", completed);
        m.put("pending", pending);
        m.put("completionRatePercent", Math.round(completionRate * 100.0) / 100.0);
        return m;
    }

    /**
     * Scheduler helper: get todos with reminders in [from, to] (not completed, not sent)
     */
    public List<Todo> findRemindersBetween(OffsetDateTime from, OffsetDateTime to) {
        return repo.findByReminderDateBetweenAndReminderSentFalseAndCompletedFalse(from, to);
    }

    /**
     * Mark a todo's reminder as sent (and persist).
     */
    public Todo markReminderSent(Todo todo) {
        todo.setReminderSent(true);
        return repo.save(todo);
    }

    /**
     * Send a reminder (delegates to NotificationService and mark sent).
     */
    public void sendReminderAndMark(Todo todo) {
        notificationService.sendReminder(todo);
        markReminderSent(todo);
    }
}
