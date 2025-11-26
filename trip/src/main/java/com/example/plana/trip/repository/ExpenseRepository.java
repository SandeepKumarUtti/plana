package com.example.plana.trip.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plana.trip.entities.Expense;

import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, String> {
     List<Expense> findByTripId(String tripId);
}
