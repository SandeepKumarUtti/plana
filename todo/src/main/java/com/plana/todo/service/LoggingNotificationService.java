package com.plana.todo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.plana.todo.domain.Todo;

@Service
public class LoggingNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationService.class);

    @Override
    public void sendReminder(Todo todo) {
        // Placeholder: log. Replace with email / push integration as needed.
        String msg = String.format("Reminder for todo [%s] (id=%s): due=%s, reminder=%s",
                todo.getTitle(), todo.getId(), todo.getDueDate(), todo.getReminderDate());
        log.info("[NOTIFICATION] {}", msg);

        // Example hooks:
        // - push notification (FCM/APNs)
        // - email using SMTP or email provider API
        // - WebSocket event to clients
    }
}
