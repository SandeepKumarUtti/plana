package com.plana.loction_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class LocationReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String title;
    private String message;

    private double targetLat;
    private double targetLng;

    private double radiusMeters; // Geofence radius

    private boolean triggered = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}

