package com.plana.loction_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.plana.loction_service.entity.LocationReminder;
import com.plana.loction_service.entity.UserLocationDTO;
import com.plana.loction_service.repository.ReminderRepository;

@Service
@RequiredArgsConstructor
public class LocationReminderService {

    private final ReminderRepository reminderRepository;

    public LocationReminder createReminder(LocationReminder reminder) {
        return reminderRepository.save(reminder);
    }
    public Iterable<LocationReminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    // Haversine formula to calculate distance
    private double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3; // radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng/2) * Math.sin(dLng/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public String checkLocation(String userId, UserLocationDTO userLocation) {
        var reminders = reminderRepository.findByUserIdAndTriggeredFalse(userId);

        for (LocationReminder r : reminders) {
            double distance = distanceInMeters(
                    userLocation.getLat(),
                    userLocation.getLng(),
                    r.getTargetLat(),
                    r.getTargetLng()
            );

            if (distance <= r.getRadiusMeters()) {
                // r.setTriggered(true);
                // reminderRepository.save(r);
                return "Reminder Triggered: " + r.getTitle();
            }
        }

        return "No reminders triggered";
    }
}

