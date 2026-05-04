package com.mobileApplication.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mobileApplication.models.RiskAssessment;

public class RiskAssessmentHistoryDTO {
	
	private Long id;
    private String riskLevel;
    private String riskSummary;
    private String inputSummary;
    private List<String> predictedConditions;
    private List<String> contributingFactors;
    private List<String> recommendedServices;
    private String preventionTips;
    private LocalDateTime assessedAt;

    // ── Factory ───────────────────────────────────────────────────────────────

    public static RiskAssessmentHistoryDTO from(RiskAssessment ra) {
        RiskAssessmentHistoryDTO dto = new RiskAssessmentHistoryDTO();
        dto.id                  = ra.getId();
        dto.riskLevel           = ra.getRiskLevel();
        dto.riskSummary         = ra.getRiskSummary();
        dto.inputSummary        = ra.getInputSummary();
        dto.predictedConditions = splitToList(ra.getPredictedConditions());
        dto.contributingFactors = splitToList(ra.getContributingFactors());
        dto.recommendedServices = splitToList(ra.getRecommendedServices());
        dto.preventionTips      = ra.getPreventionTips();
        dto.assessedAt          = ra.getAssessedAt();
        return dto;
    }

    private static List<String> splitToList(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toList());
    }
    
    public Long getId()                          { return id; }
    public String getRiskLevel()                 { return riskLevel; }
    public String getRiskSummary()               { return riskSummary; }
    public String getInputSummary()              { return inputSummary; }
    public List<String> getPredictedConditions() { return predictedConditions; }
    public List<String> getContributingFactors() { return contributingFactors; }
    public List<String> getRecommendedServices() { return recommendedServices; }
    public String getPreventionTips()            { return preventionTips; }
    public LocalDateTime getAssessedAt()         { return assessedAt; }

}
