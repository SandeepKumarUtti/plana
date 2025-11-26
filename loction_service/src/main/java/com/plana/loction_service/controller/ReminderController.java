package com.plana.loction_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.plana.loction_service.entity.LocationReminder;
import com.plana.loction_service.entity.UserLocationDTO;
import com.plana.loction_service.service.LocationReminderService;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final LocationReminderService service;

    @PostMapping
    public LocationReminder create(@RequestBody LocationReminder reminder) {
        return service.createReminder(reminder);
    }

    @GetMapping
    public Iterable<LocationReminder> getAll() {
        return service.getAllReminders();
    }

    // Mobile app calls this when location updates
    @PostMapping("/{userId}/check")
    public String checkReminder(
            @PathVariable String userId,
            @RequestBody UserLocationDTO location
    ) {
        return service.checkLocation(userId, location);
    }
}

