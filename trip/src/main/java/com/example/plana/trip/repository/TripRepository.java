package com.example.plana.trip.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.plana.trip.entities.Trip;

import java.util.Optional;
import java.util.UUID;


public interface TripRepository extends JpaRepository<Trip, String> {
    Optional<Trip> findByTitle(String title);
}

