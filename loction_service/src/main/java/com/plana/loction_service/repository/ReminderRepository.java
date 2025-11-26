package com.plana.loction_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.plana.loction_service.entity.LocationReminder;

import java.util.List;

public interface ReminderRepository extends JpaRepository<LocationReminder, String> {

    List<LocationReminder> findByUserIdAndTriggeredFalse(String userId);
}

