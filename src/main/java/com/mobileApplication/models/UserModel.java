package com.mobileApplication.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="users_table")
public class UserModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "hashed_password")
	private String hashedPassword;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "password_change_count")
	private Integer passwordChangeCount = 0;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "branch_id")
	private Long branchId;
	
	@Column(name = "deactivated_at")
	private LocalDateTime deactivatedAt;
	
	@Column(name = "role")
	private String role;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_id", insertable = false, updatable = false)
	private BranchModel branch;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.passwordChangeCount == null) this.passwordChangeCount = 0;
		if (this.status == null) this.status = "ACTIVE";
		if (this.role == null) this.role = "PATIENT";
	}

	public UserModel() {}
	
	public UserModel(String username, String hashedPassword, String email) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getPasswordChangeCount() {
		return passwordChangeCount;
	}

	public void setPasswordChangeCount(Integer passwordChangeCount) {
		this.passwordChangeCount = passwordChangeCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public BranchModel getBranch() {
		return branch;
	}

	public void setBranch(BranchModel branch) {
		this.branch = branch;
	}

	public LocalDateTime getDeactivatedAt() {
		return deactivatedAt;
	}

	public void setDeactivatedAt(LocalDateTime deactivatedAt) {
		this.deactivatedAt = deactivatedAt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
