package com.plana.todo.service;

import com.plana.todo.domain.Todo;

public interface NotificationService {
    /**
     * Send a notification for the given todo.
     * Implementations should be idempotent where possible.
     */
    void sendReminder(Todo todo);
}
