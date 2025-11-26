package com.example.plana.trip.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // no relation, plain column
    @Column(name = "trip_id")
    private String tripId;

    private String description;
    private double amount;
    private String paidBy;
}