package com.mobileApplication.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobileApplication.models.Appointments;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Long> {

    //standard JPQL range comparison
    @Query("SELECT a FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.scheduledStart >= :dayStart " +
           "AND a.scheduledStart < :dayEnd " +
           "AND a.status NOT IN ('cancelled', 'expired')")
    List<Appointments> findBookedSlots(
        @Param("dentistId") Long dentistId,
        @Param("dayStart") LocalDateTime dayStart,
        @Param("dayEnd") LocalDateTime dayEnd
    );

    //Conflict check used in confirmAppointment()
    @Query("SELECT COUNT(a) > 0 FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.scheduledStart < :slotEnd " +
           "AND a.scheduledEnd > :slotStart " +
           "AND a.status NOT IN ('cancelled', 'expired')")
    boolean hasConflict(
        @Param("dentistId") Long dentistId,
        @Param("slotStart") LocalDateTime slotStart,
        @Param("slotEnd") LocalDateTime slotEnd
    );

    //Used by getUpcomingAppointments()
    @Query("SELECT a FROM Appointments a " +
           "WHERE a.userInfoId = :userId " +
           "AND a.scheduledEnd >= :now " +
           "AND a.status NOT IN ('cancelled', 'expired') " +
           "ORDER BY a.scheduledStart ASC")
    List<Appointments> findUpcomingByUser(
        @Param("userId") Long userId,
        @Param("now") LocalDateTime now
    );
    
    //Used by cancelling an appointment
    @Query("SELECT a FROM Appointments a " +
    	       "WHERE a.userInfoId = :userId " +
    	       "AND a.status NOT IN ('cancelled', 'expired') " +
    	       "ORDER BY a.scheduledStart ASC")
    	List<Appointments> findAllActiveByUser(@Param("userId") Long userId);
    
 // ── WEB DOCTOR QUERIES — NEW ─────────────────────────────────────────────

    /** All appointments for a dentist (all time, all statuses) */
    List<Appointments> findByDentistId(Long dentistId);

    /** Today's appointments for a dentist, ordered by time */
    @Query("SELECT a FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.scheduledStart >= :dayStart " +
           "AND a.scheduledStart < :dayEnd " +
           "ORDER BY a.scheduledStart ASC")
    List<Appointments> findTodayAppointmentsByDentistId(
            @Param("dentistId") Long dentistId,
            @Param("dayStart")  LocalDateTime dayStart,
            @Param("dayEnd")    LocalDateTime dayEnd);

    /** Total count of all appointments for a dentist */
    long countByDentistId(Long dentistId);

    /** Count of today's appointments for a dentist */
    @Query("SELECT COUNT(a) FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.scheduledStart >= :dayStart " +
           "AND a.scheduledStart < :dayEnd")
    long countTodayAppointmentsByDentistId(
            @Param("dentistId") Long dentistId,
            @Param("dayStart")  LocalDateTime dayStart,
            @Param("dayEnd")    LocalDateTime dayEnd);

    /** Total distinct patients (non-null user_info_id) for a dentist */
    @Query("SELECT COUNT(DISTINCT a.userInfoId) FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.userInfoId IS NOT NULL")
    long countDistinctPatientsByDentistId(@Param("dentistId") Long dentistId);

    /** New patients this month: first-ever appointment >= monthStart */
    @Query("SELECT COUNT(DISTINCT a.userInfoId) FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.userInfoId IS NOT NULL " +
           "AND a.userInfoId NOT IN (" +
           "  SELECT a2.userInfoId FROM Appointments a2 " +
           "  WHERE a2.dentistId = :dentistId " +
           "  AND a2.userInfoId IS NOT NULL " +
           "  AND a2.scheduledStart < :monthStart" +
           ")")
    long countNewPatientsByDentistId(
            @Param("dentistId")  Long dentistId,
            @Param("monthStart") LocalDateTime monthStart);

    /** Returning patients: visited before this month AND again this month */
    @Query("SELECT COUNT(DISTINCT a.userInfoId) FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.userInfoId IS NOT NULL " +
           "AND a.scheduledStart >= :monthStart " +
           "AND a.userInfoId IN (" +
           "  SELECT a2.userInfoId FROM Appointments a2 " +
           "  WHERE a2.dentistId = :dentistId " +
           "  AND a2.userInfoId IS NOT NULL " +
           "  AND a2.scheduledStart < :monthStart" +
           ")")
    long countReturningPatientsByDentistId(
            @Param("dentistId")  Long dentistId,
            @Param("monthStart") LocalDateTime monthStart);

    /** All distinct user_info_ids for a dentist (for Patients page) */
    @Query("SELECT DISTINCT a.userInfoId FROM Appointments a " +
           "WHERE a.dentistId = :dentistId " +
           "AND a.userInfoId IS NOT NULL")
    List<Long> findDistinctPatientIdsByDentistId(@Param("dentistId") Long dentistId);
    
    
    
     
}