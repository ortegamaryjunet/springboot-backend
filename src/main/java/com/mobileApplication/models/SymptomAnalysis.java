package com.mobileApplication.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "symptom_analysis")
public class SymptomAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "symptom_description")
    private String symptomDescription;

    @Column(name = "ai_analysis", columnDefinition = "TEXT")
    private String aiAnalysis;

    @Column(name = "suggested_procedures")
    private String suggestedProcedures;

    @Column(name = "urgency_level")
    private String urgencyLevel;

    @Column(name = "estimated_duration_min")
    private int estimatedDurationMin;

    @Column(name = "recommended_specialization")
    private String recommendedSpecialization;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

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

	public String getSymptomDescription() {
		return symptomDescription;
	}

	public void setSymptomDescription(String symptomDescription) {
		this.symptomDescription = symptomDescription;
	}

	public String getAiAnalysis() {
		return aiAnalysis;
	}

	public void setAiAnalysis(String aiAnalysis) {
		this.aiAnalysis = aiAnalysis;
	}

	public String getSuggestedProcedures() {
		return suggestedProcedures;
	}

	public void setSuggestedProcedures(String suggestedProcedures) {
		this.suggestedProcedures = suggestedProcedures;
	}

	public String getUrgencyLevel() {
		return urgencyLevel;
	}

	public void setUrgencyLevel(String urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}

	public int getEstimatedDurationMin() {
		return estimatedDurationMin;
	}

	public void setEstimatedDurationMin(int estimatedDurationMin) {
		this.estimatedDurationMin = estimatedDurationMin;
	}

	public String getRecommendedSpecialization() {
		return recommendedSpecialization;
	}

	public void setRecommendedSpecialization(String recommendedSpecialization) {
		this.recommendedSpecialization = recommendedSpecialization;
	}

	public LocalDateTime getAnalyzedAt() {
		return analyzedAt;
	}

	public void setAnalyzedAt(LocalDateTime analyzedAt) {
		this.analyzedAt = analyzedAt;
	}
    
    
}
