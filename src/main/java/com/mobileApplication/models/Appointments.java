package com.mobileApplication.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table (name = "appointments")
public class Appointments {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_info_id")
    private Long userInfoId;

    @Column(name = "dentist_id")
    private Long dentistId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "symptom_analysis_id")
    private Long symptomAnalysisId;

    @Column(name = "slot_recommendation_id")
    private Long slotRecommendationId;

    @Column(name = "scheduled_start")
    private LocalDateTime scheduledStart;

    @Column(name = "scheduled_end")
    private LocalDateTime scheduledEnd;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "booking_type")
    private String bookingType;    

    @Column(name = "urgency_level")
    private String urgencyLevel; 

    @Column(name = "status")
    private String status;          

    @Column(name = "patient_notes")
    private String patientNotes;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "rescheduled_from")
    private Long rescheduledFrom;   

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "pending";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
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

	public Long getSlotRecommendationId() {
		return slotRecommendationId;
	}

	public void setSlotRecommendationId(Long slotRecommendationId) {
		this.slotRecommendationId = slotRecommendationId;
	}

	public LocalDateTime getScheduledStart() {
		return scheduledStart;
	}

	public void setScheduledStart(LocalDateTime scheduledStart) {
		this.scheduledStart = scheduledStart;
	}

	public LocalDateTime getScheduledEnd() {
		return scheduledEnd;
	}

	public void setScheduledEnd(LocalDateTime scheduledEnd) {
		this.scheduledEnd = scheduledEnd;
	}

	public int getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public String getUrgencyLevel() {
		return urgencyLevel;
	}

	public void setUrgencyLevel(String urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPatientNotes() {
		return patientNotes;
	}

	public void setPatientNotes(String patientNotes) {
		this.patientNotes = patientNotes;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public LocalDateTime getCancelledAt() {
		return cancelledAt;
	}

	public void setCancelledAt(LocalDateTime cancelledAt) {
		this.cancelledAt = cancelledAt;
	}

	public Long getRescheduledFrom() {
		return rescheduledFrom;
	}

	public void setRescheduledFrom(Long rescheduledFrom) {
		this.rescheduledFrom = rescheduledFrom;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
}
