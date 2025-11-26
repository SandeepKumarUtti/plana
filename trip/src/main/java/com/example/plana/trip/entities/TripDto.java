package com.example.plana.trip.entities;


import lombok.*;
import java.time.LocalDate;
import java.util.UUID;


@Data
@Builder
public class TripDto {
private UUID id;
private String title;
private String destination;
private LocalDate startDate;
private LocalDate endDate;
private String notes;
}
