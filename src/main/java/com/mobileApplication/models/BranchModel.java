package com.mobileApplication.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="branch_table")
public class BranchModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "branch_name")
	private String branchName;
	
	@Column(name = "branch_address")
	private String branchAddress;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "operating_hours")
	private String operatingHours;

	@Column(name = "status")
	private String status;
	
	@Column(name = "years_open")
	private String yearsOpen;
	
	public BranchModel() {}
	
	public BranchModel(String branchName, String branchAddress) {
		this.branchName = branchName;
		this.branchAddress = branchAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(LocalDateTime createAt) {
		this.createdAt = createAt;
	}

	public String getOperatingHours() {
		return operatingHours;
	}

	public void setOperatingHours(String operatingHours) {
		this.operatingHours = operatingHours;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getYearsOpen() {
		return yearsOpen;
	}

	public void setYearsOpen(String yearsOpen) {
		this.yearsOpen = yearsOpen;
	}

}
