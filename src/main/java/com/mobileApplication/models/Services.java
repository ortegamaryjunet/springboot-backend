package com.mobileApplication.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "services_table")
public class Services {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_id")
    private Long branchId;

    private String name;
    private String description;

    @Column(name = "estimated_duration_min")
    private int estimatedDurationMin;

    @Column(name = "urgency_level")
    private String urgencyLevel;

    @Column(name = "required_specialization")
    private String requiredSpecialization;

    @Column(name = "is_active")
    private boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEstimatedDurationMin() {
		return estimatedDurationMin;
	}

	public void setEstimatedDurationMin(int estimatedDurationMin) {
		this.estimatedDurationMin = estimatedDurationMin;
	}

	public String getUrgencyLevel() {
		return urgencyLevel;
	}

	public void setUrgencyLevel(String urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}

	public String getRequiredSpecialization() {
		return requiredSpecialization;
	}

	public void setRequiredSpecialization(String requiredSpecialization) {
		this.requiredSpecialization = requiredSpecialization;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
    
}
