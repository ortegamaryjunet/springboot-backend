package com.mobileApplication.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "slot_recommendations")
public class SlotRecommendation {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "dentist_id")
    private Long dentistId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "symptom_analysis_id")
    private Long symptomAnalysisId;

    @Column(name = "slot_start")
    private LocalDateTime slotStart;

    @Column(name = "slot_end")
    private LocalDateTime slotEnd;

    @Column(name = "batch_number")
    private int batchNumber;

    @Column(name = "slot_rank")
    private int slotRank;

    @Column(name = "status")
    private String status;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

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

	public Long getDentistId() {
		return dentistId;
	}

	public void setDentistId(Long dentistId) {
		this.dentistId = dentistId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getSymptomAnalysisId() {
		return symptomAnalysisId;
	}

	public void setSymptomAnalysisId(Long symptomAnalysisId) {
		this.symptomAnalysisId = symptomAnalysisId;
	}

	public LocalDateTime getSlotStart() {
		return slotStart;
	}

	public void setSlotStart(LocalDateTime slotStart) {
		this.slotStart = slotStart;
	}

	public LocalDateTime getSlotEnd() {
		return slotEnd;
	}

	public void setSlotEnd(LocalDateTime slotEnd) {
		this.slotEnd = slotEnd;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}

	public int getSlotRank() {
		return slotRank;
	}

	public void setSlotRank(int slotRank) {
		this.slotRank = slotRank;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

}
