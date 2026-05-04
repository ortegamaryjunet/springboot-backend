package com.mobileApplication.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileApplication.dto.SlotDTO;
import com.mobileApplication.dto.SymptomRequest;
import com.mobileApplication.dto.UpcomingAppointmentDTO;
import com.mobileApplication.models.Appointments;
import com.mobileApplication.models.Dentist;
import com.mobileApplication.models.Services;
import com.mobileApplication.models.SlotRecommendation;
import com.mobileApplication.models.SymptomAnalysis;
import com.mobileApplication.models.UserInfo;
import com.mobileApplication.repositories.AppointmentRepository;
import com.mobileApplication.repositories.DentistRepository;
import com.mobileApplication.repositories.MobileUserInfoRepository;
import com.mobileApplication.repositories.ServicesRepository;
import com.mobileApplication.repositories.SlotRecommendationRepository;
import com.mobileApplication.repositories.SymptomAnalysisRepository;

@Service
public class AppointmentService {

	@Autowired private DentistRepository dentistRepo;
    @Autowired private ServicesRepository servicesRepo;
    @Autowired private SlotGeneratorService slotGen;
    @Autowired private SlotRecommendationRepository slotRecRepo;
    @Autowired private AppointmentRepository appointmentRepo;
    @Autowired private GeminiService geminiService;
    @Autowired private SymptomAnalysisRepository symptomRepo;
    @Autowired private MobileUserInfoRepository userInfoRepo;

    // ─────────────────────────────────────────────
    // FLOW 1: Service-based
    // ─────────────────────────────────────────────

    public List<SlotDTO> recommendByService(Long userId, Long serviceId, int batchNumber) {

        Services service = servicesRepo.findById(serviceId)
            .orElseThrow(() -> new RuntimeException("Service not found: " + serviceId));

        List<Dentist> dentists = dentistRepo
            .findBySpecializationAndIsActiveTrue(service.getRequiredSpecialization());

        if (dentists.isEmpty())
            throw new RuntimeException("No available dentists for: " + service.getRequiredSpecialization());

        List<Long> dentistIds = dentists.stream().map(Dentist::getId).toList();

        List<SlotDTO> slots = slotGen.generateSlots(
            dentistIds,
            service.getEstimatedDurationMin(),
            service.getUrgencyLevel(),
            batchNumber
        );

        return saveAndEnrichSlots(userId, serviceId, null, slots, batchNumber, dentists, service.getName());
    }

    // ─────────────────────────────────────────────
    // FLOW 2: Symptom-based
    // ─────────────────────────────────────────────

    public Map<String, Object> recommendBySymptom(Long userId, SymptomRequest req, int batchNumber) {

        SymptomAnalysis analysis = geminiService.analyze(userId, req);
        SymptomAnalysis saved = symptomRepo.save(analysis);

        List<Dentist> dentists = dentistRepo
            .findBySpecializationAndIsActiveTrue(analysis.getRecommendedSpecialization());

        if (dentists.isEmpty()) {
            dentists = dentistRepo.findBySpecializationAndIsActiveTrue("General Dentistry");
        }

        if (dentists.isEmpty())
            throw new RuntimeException("No available dentists found.");

        List<Long> dentistIds = dentists.stream().map(Dentist::getId).toList();

        List<SlotDTO> slots = slotGen.generateSlots(
            dentistIds,
            analysis.getEstimatedDurationMin(),
            analysis.getUrgencyLevel(),
            batchNumber
        );

        List<SlotDTO> enriched = saveAndEnrichSlots(
            userId,
            null,
            saved.getId(),
            slots,
            batchNumber,
            dentists,
            analysis.getSuggestedProcedures()
        );

        return Map.of("analysis", saved, "slots", enriched);
    }

    public List<SlotDTO> recommendBySymptomId(Long userId, Long symptomAnalysisId, int batchNumber) {

        SymptomAnalysis analysis = symptomRepo.findById(symptomAnalysisId)
            .orElseThrow(() -> new RuntimeException("Symptom analysis not found: " + symptomAnalysisId));

        List<Dentist> dentists = dentistRepo
            .findBySpecializationAndIsActiveTrue(analysis.getRecommendedSpecialization());

        if (dentists.isEmpty()) {
            dentists = dentistRepo.findBySpecializationAndIsActiveTrue("General Dentistry");
        }

        if (dentists.isEmpty())
            throw new RuntimeException("No available dentists found.");

        List<Long> dentistIds = dentists.stream().map(Dentist::getId).toList();

        List<SlotDTO> slots = slotGen.generateSlots(
            dentistIds,
            analysis.getEstimatedDurationMin(),
            analysis.getUrgencyLevel(),
            batchNumber
        );

        return saveAndEnrichSlots(
            userId,
            null,
            symptomAnalysisId,
            slots,
            batchNumber,
            dentists,
            analysis.getSuggestedProcedures()
        );
    }

    // ─────────────────────────────────────────────
    // CONFIRM APPOINTMENT
    // ─────────────────────────────────────────────

    public Appointments confirmAppointment(Long userId, Long slotRecommendationId, String patientNotes) {

        SlotRecommendation rec = slotRecRepo.findById(slotRecommendationId)
            .orElseThrow(() -> new RuntimeException("Slot recommendation not found"));

        if (LocalDateTime.now().isAfter(rec.getExpiresAt()))
            throw new RuntimeException("This slot has expired. Please request new slots.");

        boolean conflict = appointmentRepo
            .hasConflict(rec.getDentistId(), rec.getSlotStart(), rec.getSlotEnd());

        if (conflict)
            throw new RuntimeException("Slot no longer available. Please choose another.");

        Dentist dentist = dentistRepo.findById(rec.getDentistId())
            .orElseThrow(() -> new RuntimeException("Dentist not found"));

        UserInfo userInfo = userInfoRepo.findAll().stream()
            .filter(u -> u.getUserModel() != null && u.getUserModel().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User profile not found"));

        Appointments appt = new Appointments();
        appt.setUserInfoId(userInfo.getId());
        appt.setDentistId(rec.getDentistId());
        appt.setServiceId(rec.getServiceId());
        appt.setSymptomAnalysisId(rec.getSymptomAnalysisId());
        appt.setSlotRecommendationId(slotRecommendationId);
        appt.setScheduledStart(rec.getSlotStart());
        appt.setScheduledEnd(rec.getSlotEnd());
        appt.setDurationMinutes((int) java.time.Duration
            .between(rec.getSlotStart(), rec.getSlotEnd()).toMinutes());
        appt.setBookingType(rec.getSymptomAnalysisId() != null ? "symptom" : "service");
        appt.setPatientNotes(patientNotes);
        appt.setStatus("confirmed");
        appt.setBranchId(dentist.getBranchId());

        Appointments saved = appointmentRepo.save(appt);

        rec.setStatus("confirmed");
        slotRecRepo.save(rec);

        expireOtherSlotsInBatch(rec.getUserInfoId(), rec.getBatchNumber(), slotRecommendationId);

        return saved;
    }

    // ─────────────────────────────────────────────
    // SLOT ENRICHMENT
    // ─────────────────────────────────────────────

    private List<SlotDTO> saveAndEnrichSlots(Long userId, Long serviceId, Long symptomAnalysisId,
                                              List<SlotDTO> slots, int batchNumber,
                                              List<Dentist> dentists, String serviceName) {

        List<SlotDTO> enriched = new ArrayList<>();
        int rank = 1;

        Map<Long, String> dentistNameMap = new java.util.HashMap<>();
        for (Dentist d : dentists) {
            dentistNameMap.put(d.getId(), "Dr. " + d.getFirstname() + " " + d.getSurname());
        }

        UserInfo userInfo = userInfoRepo.findAll().stream()
            .filter(u -> u.getUserModel() != null && u.getUserModel().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User profile not found"));

        for (SlotDTO slot : slots) {

            SlotRecommendation rec = new SlotRecommendation();
            rec.setUserInfoId(userInfo.getId());
            rec.setServiceId(serviceId);
            rec.setSymptomAnalysisId(symptomAnalysisId);
            rec.setDentistId(slot.getDentistId());
            rec.setSlotStart(slot.getSlotStart());
            rec.setSlotEnd(slot.getSlotEnd());
            rec.setBatchNumber(batchNumber);
            rec.setSlotRank(rank++);
            rec.setStatus("pending");
            rec.setGeneratedAt(LocalDateTime.now());
            rec.setExpiresAt(LocalDateTime.now().plusMinutes(15));

            SlotRecommendation saved = slotRecRepo.save(rec);

            slot.setSlotRecommendationId(saved.getId());
            slot.setServiceId(serviceId);
            slot.setDentistName(dentistNameMap.getOrDefault(slot.getDentistId(), "Unknown"));
            slot.setServiceName(serviceName);

            enriched.add(slot);
        }

        return enriched;
    }

    private void expireOtherSlotsInBatch(Long userId, int batchNumber, Long confirmedId) {
        List<SlotRecommendation> batch = slotRecRepo.findByUserInfoIdAndBatchNumber(userId, batchNumber);
        for (SlotRecommendation r : batch) {
            if (!r.getId().equals(confirmedId) && "pending".equals(r.getStatus())) {
                r.setStatus("expired");
                slotRecRepo.save(r);
            }
        }
    }

    // ─────────────────────────────────────────────
    // ✅ FIXED UPCOMING (CRITICAL FIX APPLIED)
    // ─────────────────────────────────────────────

    public List<Appointments> getUpcomingAppointments(Long userId) {

        UserInfo userInfo = userInfoRepo.findAll().stream()
            .filter(u -> u.getUserModel() != null && u.getUserModel().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User profile not found"));

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(30);

        return appointmentRepo.findUpcomingByUser(userInfo.getId(), cutoff);
    }

    public List<UpcomingAppointmentDTO> getUpcomingAppointmentsEnriched(Long userId) {

        UserInfo userInfo = userInfoRepo.findAll().stream()
            .filter(u -> u.getUserModel() != null && u.getUserModel().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User profile not found"));

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(30);

        List<Appointments> appts = appointmentRepo.findUpcomingByUser(userInfo.getId(), cutoff);
        List<UpcomingAppointmentDTO> result = new ArrayList<>();

        for (Appointments appt : appts) {

            String serviceName = servicesRepo.findById(appt.getServiceId())
                .map(s -> s.getName())
                .orElse("Appointment");

            String dentistName = dentistRepo.findById(appt.getDentistId())
                .map(d -> "Dr. " + d.getFirstname() + " " + d.getSurname())
                .orElse("Doctor");

            result.add(new UpcomingAppointmentDTO(
                appt.getId(),
                serviceName,
                dentistName,
                appt.getScheduledStart(),
                appt.getScheduledEnd(),
                appt.getDurationMinutes(),
                appt.getStatus()
            ));
        }

        return result;
    }
    
    public void cancelAppointment(Long userId, Long appointmentId) {

        // 🔥 Convert userId → userInfoId
        UserInfo userInfo = userInfoRepo.findAll().stream()
            .filter(u -> u.getUserModel() != null && u.getUserModel().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User profile not found"));

        Appointments appt = appointmentRepo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));

        if (!appt.getUserInfoId().equals(userInfo.getId()))
            throw new RuntimeException("Appointment does not belong to this user.");

        if ("cancelled".equals(appt.getStatus()))
            throw new RuntimeException("Appointment is already cancelled.");

        // Get related IDs
        Long slotRecId = appt.getSlotRecommendationId();
        Long saId      = appt.getSymptomAnalysisId();

        // Delete appointment
        appointmentRepo.delete(appt);

        // Delete SlotRecommendation
        if (slotRecId != null) {
            slotRecRepo.findById(slotRecId).ifPresent(slotRecRepo::delete);
        }

        // Delete SymptomAnalysis if no other appointments use it
        if (saId != null) {
            boolean stillUsed = appointmentRepo
                .findAllActiveByUser(userInfo.getId())
                .stream()
                .anyMatch(a -> saId.equals(a.getSymptomAnalysisId()));

            if (!stillUsed) {
                symptomRepo.deleteById(saId);
            }
        }
    }
    
}
