package com.mobileApplication.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobileApplication.dto.RiskAssessmentHistoryDTO;
import com.mobileApplication.dto.RiskAssessmentRequest;
import com.mobileApplication.services.RiskAssessmentService;

@RestController
@RequestMapping ("/api/risks")
public class RiskAssessmentController {
	
	private final RiskAssessmentService service;

    public RiskAssessmentController(RiskAssessmentService service) {
        this.service = service;
    }

    /**
     * 200 → success with assessment payload
     * 503 → Gemini models exhausted / temporarily unavailable
     * 500 → unexpected server error
     */
    @PostMapping
    public ResponseEntity<?> assess(@RequestBody RiskAssessmentRequest req) {
        try {
            Map<String, Object> result = service.runAssessment(req);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            // Covers Gemini exhaustion, parse failure, network errors
            return ResponseEntity
                .status(503)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(500)
                .body(Map.of("error", "An unexpected error occurred. Please try again."));
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> history(@PathVariable Long userId) {
        try {
            List<RiskAssessmentHistoryDTO> history = service.getHistory(userId)
                    .stream()
                    .map(RiskAssessmentHistoryDTO::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Could not load assessment history."));
        }
    }

}
