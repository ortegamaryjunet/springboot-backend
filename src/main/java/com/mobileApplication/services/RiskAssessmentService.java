package com.mobileApplication.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mobileApplication.dto.RiskAssessmentRequest;
import com.mobileApplication.models.RiskAssessment;
import com.mobileApplication.repositories.RiskAssessmentRepository;

@Service
public class RiskAssessmentService {

    private final GeminiRiskService geminiRiskService;
    private final RiskAssessmentRepository repository;

    public RiskAssessmentService(GeminiRiskService geminiRiskService,
    		RiskAssessmentRepository repository) {
        this.geminiRiskService = geminiRiskService;
        this.repository        = repository;
    }

    /**
     * Run Gemini risk assessment, persist the result, and return a response
     * map ready to be serialised by the controller.
     */
    public java.util.Map<String, Object> runAssessment(RiskAssessmentRequest req) {

        // Call Gemini (throws RuntimeException on failure)
    	RiskAssessment assessment = geminiRiskService.assess(req.getUserId(), req);

        // Persist
    	RiskAssessment saved = repository.save(assessment);

        // Build response — split comma-separated strings into proper lists
        // so the frontend can render them directly without extra parsing.
        java.util.Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("id",                   saved.getId());
        response.put("riskLevel",            saved.getRiskLevel());
        response.put("riskSummary",          saved.getRiskSummary());
        response.put("predictedConditions",  splitToList(saved.getPredictedConditions()));
        response.put("contributingFactors",  splitToList(saved.getContributingFactors()));
        response.put("recommendedServices",  splitToList(saved.getRecommendedServices()));
        response.put("preventionTips",       saved.getPreventionTips());
        response.put("assessedAt",           saved.getAssessedAt());

        return response;
    }

    /**
     * Fetch the most recent assessment for a user (history screen use-case).
     */
    public List<RiskAssessment> getHistory(Long userId) {
        return repository.findByUserInfoIdOrderByAssessedAtDesc(userId);
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private List<String> splitToList(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toList());
    }
    
}
