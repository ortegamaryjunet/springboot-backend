package com.mobileApplication.services.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileApplication.dto.web.AppointmentRowDTO;
import com.mobileApplication.dto.web.RecepWalkInRequest;
import com.mobileApplication.models.Appointments;
import com.mobileApplication.models.Dentist;
import com.mobileApplication.models.Services;
import com.mobileApplication.repositories.DentistRepository;
import com.mobileApplication.repositories.ServicesRepository;
import com.mobileApplication.repositories.web.RecepAppointment;

@Service
public class RecepAppointmentService {
	
	@Autowired private RecepAppointment apptRepo;
    @Autowired private DentistRepository dentistRepo;
    @Autowired private ServicesRepository servicesRepo;
    
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter DAY_FMT  = DateTimeFormatter.ofPattern("EEE");

    // ── Fetch & enrich ───────────────────────────────────────────────────

    /** Returns today's full appointment list — used by initial page load. */
    public List<AppointmentRowDTO> getTodaysAppointments() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end   = start.plusDays(1);
        return enrich(apptRepo.findTodaysAppointments(start, end));
    }

    /** Filter by dentist (pass null to skip). */
    public List<AppointmentRowDTO> getTodaysAppointments(Long dentistId, Long serviceId) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end   = start.plusDays(1);

        List<Appointments> raw;
        if (dentistId != null) {
            raw = apptRepo.findTodaysByDentist(dentistId, start, end);
        } else if (serviceId != null) {
            raw = apptRepo.findTodaysByService(serviceId, start, end);
        } else {
            raw = apptRepo.findTodaysAppointments(start, end);
        }
        return enrich(raw);
    }

    // ── Walk-in booking ──────────────────────────────────────────────────

    /**
     * Creates a walk-in appointment. Walk-ins use bookingType = "walk_in"
     * and store the patient name in patientNotes (no user account required).
     * A conflict check is performed against the chosen dentist's schedule.
     */
    public AppointmentRowDTO addWalkIn(RecepWalkInRequest req) {

        Services service = servicesRepo.findById(req.getServiceId())
            .orElseThrow(() -> new RuntimeException("Service not found"));

        Dentist dentist = dentistRepo.findById(req.getDentistId())
            .orElseThrow(() -> new RuntimeException("Dentist not found"));

        LocalDateTime slotStart = LocalDateTime.parse(req.getScheduledStart());

	     // Clinic hours guard: 10:00 AM - 7:00 PM
	     LocalTime clinicOpen  = LocalTime.of(10, 0);
	     LocalTime clinicClose = LocalTime.of(19, 0);
	     LocalTime slotTime    = slotStart.toLocalTime();
	
	     if (slotTime.isBefore(clinicOpen) || slotTime.isAfter(clinicClose)) {
	         throw new RuntimeException(
	             "Appointment must be scheduled between 10:00 AM and 7:00 PM."
	         );
	     }
     
        LocalDateTime slotEnd   = slotStart.plusMinutes(service.getEstimatedDurationMin());

        // Conflict guard — scheduled patients always keep their slot
        boolean conflict = apptRepo.hasConflict(req.getDentistId(), slotStart, slotEnd);
        if (conflict) {
            throw new RuntimeException(
                "That time slot conflicts with an existing appointment for " +
                "Dr. " + dentist.getFirstname() + " " + dentist.getSurname() +
                ". Please choose a different time or dentist."
            );
        }

        Appointments appt = new Appointments();
        appt.setDentistId(req.getDentistId());
        appt.setBranchId(dentist.getBranchId());
        appt.setServiceId(req.getServiceId());
        appt.setScheduledStart(slotStart);
        appt.setScheduledEnd(slotEnd);
        appt.setDurationMinutes(service.getEstimatedDurationMin());
        appt.setBookingType("walk_in");
        appt.setUrgencyLevel(service.getUrgencyLevel());
        // patientName stored in patientNotes for walk-ins (no user account)
        appt.setPatientNotes("[Walk-in] " + req.getPatientName() +
            (req.getPatientNotes() != null && !req.getPatientNotes().isBlank()
                ? " — " + req.getPatientNotes() : ""));
        appt.setStatus("pending");

        Appointments saved = apptRepo.save(appt);
        return toDTO(saved, "Dr. " + dentist.getFirstname() + " " + dentist.getSurname(),
                     service.getName());
    }

    // ── Status transitions ───────────────────────────────────────────────

    /**
     * Mark arrived → moves from Pending list into the Queue.
     * Status becomes "arrived".
     */
    public void markArrived(Long apptId) {
        Appointments appt = apptRepo.findById(apptId)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + apptId));

        if ("cancelled".equals(appt.getStatus()))
            throw new RuntimeException("Cannot mark a cancelled appointment as arrived.");

        appt.setStatus("arrived");
        apptRepo.save(appt);
    }

    /**
     * Mark as done/completed — removes from queue.
     */
    public void markDone(Long apptId) {
        Appointments appt = apptRepo.findById(apptId)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + apptId));
        appt.setStatus("completed");
        apptRepo.save(appt);
    }

    /**
     * Cancel an appointment from the web side.
     */
    public void cancelAppointment(Long apptId) {
        Appointments appt = apptRepo.findById(apptId)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + apptId));

        if ("cancelled".equals(appt.getStatus()))
            throw new RuntimeException("Appointment is already cancelled.");

        appt.setStatus("cancelled");
        appt.setCancelledAt(LocalDateTime.now());
        appt.setCancellationReason("Cancelled by receptionist");
        apptRepo.save(appt);
    }

    // ── Enrichment helpers ───────────────────────────────────────────────

    private List<AppointmentRowDTO> enrich(List<Appointments> list) {

        // Build lookup maps to avoid N+1 queries
        Map<Long, String> dentistNames  = buildDentistMap(list);
        Map<Long, String> serviceNames  = buildServiceMap(list);

        return list.stream()
            .map(a -> toDTO(a,
                dentistNames.getOrDefault(a.getDentistId(), "Unknown Dentist"),
                serviceNames.getOrDefault(a.getServiceId(), "Consultation")))
            .collect(Collectors.toList());
    }

    private Map<Long, String> buildDentistMap(List<Appointments> list) {
        Map<Long, String> map = new HashMap();
        list.stream()
            .filter(a -> a.getDentistId() != null)
            .map(Appointments::getDentistId)
            .distinct()
            .forEach(id -> dentistRepo.findById(id).ifPresent(d ->
                map.put(id, "Dr. " + d.getFirstname() + " " + d.getSurname())));
        return map;
    }

    private Map<Long, String> buildServiceMap(List<Appointments> list) {
        Map<Long, String> map = new HashMap<>();
        list.stream()
            .filter(a -> a.getServiceId() != null)
            .map(Appointments::getServiceId)
            .distinct()
            .forEach(id -> servicesRepo.findById(id).ifPresent(s ->
                map.put(id, s.getName())));
        return map;
    }

    private AppointmentRowDTO toDTO(Appointments a, String dentistName, String serviceName) {

        LocalDateTime start = a.getScheduledStart();
        LocalDateTime end   = a.getScheduledEnd();

        // For walk-ins the "patient name" is embedded in patientNotes
        String patientName = resolvePatientName(a);

        return new AppointmentRowDTO(
            a.getId(),
            patientName,
            dentistName,
            serviceName,
            start != null ? start.format(TIME_FMT) : "--",
            end   != null ? end.format(TIME_FMT)   : "--",
            start != null ? start.format(DAY_FMT)  : "--",
            start != null ? start.getDayOfMonth()   : 0,
            a.getStatus(),
            a.getBookingType() != null ? a.getBookingType() : "scheduled",
            a.getUrgencyLevel(),
            a.getPatientNotes()
        );
    }

    /**
     * Walk-ins store "[Walk-in] Name — notes" in patientNotes.
     * Scheduled patients have a userInfoId but we only store the ID —
     * receptionist sees a generic label; extend this if you join UserInfo later.
     */
    private String resolvePatientName(Appointments a) {
        if ("walk_in".equals(a.getBookingType()) && a.getPatientNotes() != null) {
            String notes = a.getPatientNotes();
            // Strip the "[Walk-in] " prefix to show just the name
            if (notes.startsWith("[Walk-in] ")) {
                String after = notes.substring(10);
                int dash = after.indexOf(" — ");
                return dash > 0 ? after.substring(0, dash) : after;
            }
        }
        // Scheduled mobile patient — show user ID for now
        // (extend here to join with your UserInfo table if you want the full name)
        if (a.getUserInfoId() != null) {
            return "Patient #" + a.getUserInfoId();
        }
        return "Walk-in Patient";
    }
    
    public void linkPatientAccount(Long appointmentId, Long userInfoId) {
        Appointments appt = apptRepo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appt.setUserInfoId(userInfoId);
        apptRepo.save(appt);
    }

}
