package com.example.plana.trip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.plana.trip.entities.Expense;
import com.example.plana.trip.entities.ItineraryItem;
import com.example.plana.trip.entities.PackageItem;
import com.example.plana.trip.entities.Trip;
import com.example.plana.trip.entities.TripDto;
import com.example.plana.trip.repository.ExpenseRepository;
import com.example.plana.trip.repository.ItineraryRepository;
import com.example.plana.trip.repository.PackingRepository;
import com.example.plana.trip.repository.TripRepository;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired private TripRepository tripRepo;
    @Autowired private PackingRepository pkgRepo;
    @Autowired private ExpenseRepository expRepo;
    @Autowired private ItineraryRepository itineraryRepo;

    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        return tripRepo.save(trip);
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripRepo.findAll();
    }

    @GetMapping("/{id}")
    public Trip getTrip(@PathVariable String id) {
        return tripRepo.findById(id).orElse(null);
    }

    @PostMapping("/{tripId}/itinerary-items")
    public ItineraryItem addItineraryItem(
            @PathVariable String tripId,
            @RequestBody ItineraryItem item
    ) {
        item.setTripId(tripId);
        return itineraryRepo.save(item);
    }

    @GetMapping("/{tripId}/itinerary-items")
    public List<ItineraryItem> getItineraryItems(@PathVariable String tripId) {
        return itineraryRepo.findByTripId(tripId);
    }

    // -------- PACKAGE ITEMS ---------

    @PostMapping("/{tripId}/package-items")
    public PackageItem addPackageItem(
            @PathVariable String tripId,
            @RequestBody PackageItem item
    ) {
        item.setId(tripId);
        return pkgRepo.save(item);
    }

    @GetMapping("/{tripId}/package-items")
    public List<PackageItem> getPackageItems(@PathVariable String tripId) {
        return pkgRepo.findByTripId(tripId);
    }

    @GetMapping("/by-trip-title")
    public ResponseEntity<List<PackageItem>> getByTripTitle(
            @RequestParam String title) {
        return ResponseEntity.ok(pkgRepo.findByTrip_Title(title));
    }


    // -------- EXPENSES ---------

    @PostMapping("/{tripId}/expenses")
    public Expense addExpense(
            @PathVariable String tripId,
            @RequestBody Expense expense
    ) {
        expense.setTripId(tripId);
        return expRepo.save(expense);
    }

    @GetMapping("/{tripId}/expenses")
    public List<Expense> getExpenses(@PathVariable String tripId) {
        return expRepo.findByTripId(tripId);
    }
}
