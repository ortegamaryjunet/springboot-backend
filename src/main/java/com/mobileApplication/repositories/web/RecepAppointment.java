package com.mobileApplication.repositories.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mobileApplication.models.Appointments;

public interface RecepAppointment extends JpaRepository<Appointments, Long> {

	long countByStatus(String status);
	
	long countByStatusAndScheduledStartGreaterThanEqual(String status, LocalDateTime now);
	
	// ── Today's appointments (scheduled first, then walk-ins, then by time) ─
    // bookingType: "service" and "symptom" = scheduled mobile patients
    //              "walk_in"               = receptionist-added walk-ins
    // Scheduled patients always sort before walk-ins because
    //   "service" < "walk_in"  alphabetically (ASC).
    @Query("SELECT a FROM Appointments a " +
           "WHERE a.scheduledStart >= :dayStart " +
           "AND   a.scheduledStart <  :dayEnd " +
           "AND   a.status NOT IN ('cancelled', 'expired') " +
           "ORDER BY " +
           "  CASE WHEN a.bookingType = 'walk_in' THEN 1 ELSE 0 END ASC, " +
           "  a.scheduledStart ASC")
    List<Appointments> findTodaysAppointments(
        @Param("dayStart") LocalDateTime dayStart,
        @Param("dayEnd")   LocalDateTime dayEnd
    );

    // ── Filter by dentist for today ──────────────────────────────────────
    @Query("SELECT a FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND   a.scheduledStart >= :dayStart " +
           "AND   a.scheduledStart <  :dayEnd " +
           "AND   a.status NOT IN ('cancelled', 'expired') " +
           "ORDER BY " +
           "  CASE WHEN a.bookingType = 'walk_in' THEN 1 ELSE 0 END ASC, " +
           "  a.scheduledStart ASC")
    List<Appointments> findTodaysByDentist(
        @Param("dentistId") Long dentistId,
        @Param("dayStart")  LocalDateTime dayStart,
        @Param("dayEnd")    LocalDateTime dayEnd
    );

    // ── Filter by service for today ──────────────────────────────────────
    @Query("SELECT a FROM Appointments a " +
           "WHERE a.serviceId = :serviceId " +
           "AND   a.scheduledStart >= :dayStart " +
           "AND   a.scheduledStart <  :dayEnd " +
           "AND   a.status NOT IN ('cancelled', 'expired') " +
           "ORDER BY " +
           "  CASE WHEN a.bookingType = 'walk_in' THEN 1 ELSE 0 END ASC, " +
           "  a.scheduledStart ASC")
    List<Appointments> findTodaysByService(
        @Param("serviceId") Long serviceId,
        @Param("dayStart")  LocalDateTime dayStart,
        @Param("dayEnd")    LocalDateTime dayEnd
    );

    // ── Conflict check before adding a walk-in ───────────────────────────
    @Query("SELECT COUNT(a) > 0 FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND   a.scheduledStart < :slotEnd " +
           "AND   a.scheduledEnd   > :slotStart " +
           "AND   a.status NOT IN ('cancelled', 'expired')")
    boolean hasConflict(
        @Param("dentistId")  Long dentistId,
        @Param("slotStart")  LocalDateTime slotStart,
        @Param("slotEnd")    LocalDateTime slotEnd
    );
	
	
}
