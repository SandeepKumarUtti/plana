package com.example.plana.trip.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plana.trip.entities.PackageItem;


public interface PackingRepository extends JpaRepository<PackageItem, String> {
        List<PackageItem> findByTripId(String tripId);
        List<PackageItem> findByTrip_Title(String title);

}