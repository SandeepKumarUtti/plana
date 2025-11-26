package com.plana.todo.scheduler;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.plana.todo.domain.Todo;
import com.plana.todo.service.TodoService;

import java.time.*;

import java.util.List;

@Component
public class ReminderScheduler {
    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);

    private final TodoService todoService;
    private final ZoneId zoneId;

    /**
     * lookaheadSeconds: how far ahead (from now) to consider reminders as due
     * Example: 60 -> reminders occurring within next 60 seconds
     */
    private final long lookaheadSeconds;

    public ReminderScheduler(TodoService todoService,
                             @Value("${todo.reminder.lookahead-seconds:60}") long lookaheadSeconds,
                             @Value("${todo.reminder.timezone:Asia/Kolkata}") String zone) {
        this.todoService = todoService;
        this.lookaheadSeconds = lookaheadSeconds;
        this.zoneId = ZoneId.of(zone);
    }

    @PostConstruct
    public void init() {
        log.info("ReminderScheduler initialized with lookaheadSeconds={} zone={}", lookaheadSeconds, zoneId);
    }

    /**
     * Runs on a fixed cron — configurable default every minute.
     * The scheduler runs in the timezone specified (Asia/Kolkata by default).
     */
    @Scheduled(cron = "${todo.reminder.cron:0 * * * * *}", zone = "${todo.reminder.timezone:Asia/Kolkata}")
    @Transactional(readOnly = true)
    public void scanAndSendReminders() {
        // compute window [now, now + lookaheadSeconds]
        ZonedDateTime nowZ = ZonedDateTime.now(zoneId);
        ZonedDateTime toZ = nowZ.plusSeconds(lookaheadSeconds);

        OffsetDateTime from = nowZ.toOffsetDateTime();
        OffsetDateTime to = toZ.toOffsetDateTime();

        log.debug("Scanning reminders from {} to {}", from, to);
        List<Todo> todos = todoService.findRemindersBetween(from, to);
        if (todos.isEmpty()) {
            log.debug("No reminders found in window.");
            return;
        }
        log.info("Found {} reminders to process", todos.size());
        for (Todo t : todos) {
            try {
                todoService.sendReminderAndMark(t);
                log.info("Reminder processed for todo id={}", t.getId());
            } catch (Exception e) {
                // catch per-item so others continue; log for later retry strategies
                log.error("Failed to send reminder for todo id=" + t.getId(), e);
            }
        }
    }
}

