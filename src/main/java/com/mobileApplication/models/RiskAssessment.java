package com.mobileApplication.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "risk_assessments")
public class RiskAssessment {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "users_info_id", nullable = false)
    private Long userInfoId;

    // ── Raw snapshot of what the user submitted ──────────────────────────────
    @Column(name = "input_summary", columnDefinition = "TEXT")
    private String inputSummary;

    // ── Gemini AI outputs ────────────────────────────────────────────────────

    @Column(name = "risk_level", length = 20)
    private String riskLevel;                   // low | moderate | high

    @Column(name = "risk_summary", columnDefinition = "TEXT")
    private String riskSummary;                 // 2–3 sentence plain-English summary

    @Column(name = "predicted_conditions", columnDefinition = "TEXT")
    private String predictedConditions;         // comma-separated

    @Column(name = "contributing_factors", columnDefinition = "TEXT")
    private String contributingFactors;         // comma-separated habits/conditions driving the risk

    @Column(name = "recommended_services", columnDefinition = "TEXT")
    private String recommendedServices;         // comma-separated service names to show the user

    @Column(name = "prevention_tips", columnDefinition = "TEXT")
    private String preventionTips;              // actionable lifestyle tips

    @Column(name = "assessed_at")
    private LocalDateTime assessedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(Long userInfoId) {
		this.userInfoId = userInfoId;
	}

	public String getInputSummary() {
		return inputSummary;
	}

	public void setInputSummary(String inputSummary) {
		this.inputSummary = inputSummary;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getRiskSummary() {
		return riskSummary;
	}

	public void setRiskSummary(String riskSummary) {
		this.riskSummary = riskSummary;
	}

	public String getPredictedConditions() {
		return predictedConditions;
	}

	public void setPredictedConditions(String predictedConditions) {
		this.predictedConditions = predictedConditions;
	}

	public String getContributingFactors() {
		return contributingFactors;
	}

	public void setContributingFactors(String contributingFactors) {
		this.contributingFactors = contributingFactors;
	}

	public String getRecommendedServices() {
		return recommendedServices;
	}

	public void setRecommendedServices(String recommendedServices) {
		this.recommendedServices = recommendedServices;
	}

	public String getPreventionTips() {
		return preventionTips;
	}

	public void setPreventionTips(String preventionTips) {
		this.preventionTips = preventionTips;
	}

	public LocalDateTime getAssessedAt() {
		return assessedAt;
	}

	public void setAssessedAt(LocalDateTime assessedAt) {
		this.assessedAt = assessedAt;
	}

}
