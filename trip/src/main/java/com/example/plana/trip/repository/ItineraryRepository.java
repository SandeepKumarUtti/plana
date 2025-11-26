package com.example.plana.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plana.trip.entities.ItineraryItem;

public interface ItineraryRepository extends JpaRepository<ItineraryItem, String> {
        List<ItineraryItem> findByTripId(String tripId);

}
