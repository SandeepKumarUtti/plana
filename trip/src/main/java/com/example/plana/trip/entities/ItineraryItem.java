package com.example.plana.trip.entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itinerary_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "trip_id")
    private String tripId;
    private String day;
    private String activity;
    private String location;
    private String time;
}