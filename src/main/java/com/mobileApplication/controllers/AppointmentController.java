package com.mobileApplication.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobileApplication.dto.SlotDTO;
import com.mobileApplication.dto.SymptomRequest;
import com.mobileApplication.dto.UpcomingAppointmentDTO;
import com.mobileApplication.models.Appointments;
import com.mobileApplication.services.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired private AppointmentService appointmentService;

    //Flow 1: Service-based ────────────────────────────────────────────────
    @PostMapping("/recommend-by-service")
    public ResponseEntity<?> byService(@RequestBody Map<String, Object> req) {
        try {
            Long userId     = Long.valueOf(req.get("userId").toString());
            Long serviceId  = Long.valueOf(req.get("serviceId").toString());
            int batchNumber = Integer.parseInt(req.getOrDefault("batchNumber", 1).toString());

            List<SlotDTO> slots = appointmentService.recommendByService(userId, serviceId, batchNumber);

            if (slots.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "message", "No available slots found. Try a later batch.",
                    "slots", slots
                ));
            }

            return ResponseEntity.ok(slots);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/recommend-by-symptom")
    public ResponseEntity<?> bySymptom(@RequestBody SymptomRequest req) {
        try {
            Map<String, Object> result = appointmentService.recommendBySymptom(
                req.getUserId(), req, req.getBatchNumber()
            );
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
           
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));

        } catch (RuntimeException e) {
           
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
        
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error",
                    "Analysis is currently unavailable. Please try again later " +
                    "or use the Browse by Service option to book directly."));
        }
    }

    //Flow 2b: More slots using cached symptomAnalysisId ───────────────────
    @PostMapping("/recommend-by-symptom-id")
    public ResponseEntity<?> bySymptomId(@RequestBody Map<String, Object> req) {
        try {
            Long userId            = Long.valueOf(req.get("userId").toString());
            Long symptomAnalysisId = Long.valueOf(req.get("symptomAnalysisId").toString());
            int  batchNumber       = Integer.parseInt(req.getOrDefault("batchNumber", 1).toString());

            List<SlotDTO> slots = appointmentService.recommendBySymptomId(userId, symptomAnalysisId, batchNumber);

            if (slots.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "message", "No available slots found. Try a later batch.",
                    "slots", slots
                ));
            }

            return ResponseEntity.ok(slots);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody Map<String, Object> req) {
        try {
            Long userId               = Long.valueOf(req.get("userId").toString());
            Long slotRecommendationId = Long.valueOf(req.get("slotRecommendationId").toString());
            String patientNotes       = req.getOrDefault("patientNotes", "").toString();

            Appointments saved = appointmentService.confirmAppointment(
                userId, slotRecommendationId, patientNotes
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/upcoming/{userId}")
    public ResponseEntity<?> upcoming(@PathVariable Long userId) {
        try {
            List<UpcomingAppointmentDTO> appts = appointmentService.getUpcomingAppointmentsEnriched(userId);
            return ResponseEntity.ok(appts);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
     
    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancel(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, Object> req) {
        try {
            Long userId = Long.valueOf(req.get("userId").toString());
            appointmentService.cancelAppointment(userId, appointmentId);
            return ResponseEntity.ok(Map.of("message", "Appointment cancelled successfully."));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
}
