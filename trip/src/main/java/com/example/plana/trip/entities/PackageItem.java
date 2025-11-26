package com.example.plana.trip.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "package_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private String itemName;
    private boolean packed;
}