package com.mobileApplication.models.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import com.mobileApplication.models.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "employee_info")
public class AdminEmployeeInfo {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name="user_id")
	    private Long userId;

	    @Column(name="branch_id")
	    private Long branchId;

	    @Column(name="first_name")
	    private String firstName;

	    @Column(name="middle_name")
	    private String middleName;

	    @Column(name="last_name")
	    private String lastName;

	    private String email;

	    @Column(name="phone_number")
	    private String phoneNumber;

	    private String role;
	    private String gender;

	    @Column(name="date_of_birth")
	    private LocalDate dateOfBirth;

	    private String address;

	    @Column(name="is_active")
	    private Boolean isActive;

	    @Column(name="created_at")
	    private LocalDateTime createdAt;

	    @Column(name="updated_at")
	    private LocalDateTime updatedAt;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", insertable = false, updatable = false)
	    private UserModel userModel;

	    public Integer getAge() {
	        if (dateOfBirth == null) return null;
	        return Period.between(dateOfBirth, LocalDate.now()).getYears();
	    }

	    public String getDisplayRole() {
	        if (role == null) return "-";

	        return switch (role.toUpperCase()) {
	            case "ADMIN" -> "Admin";
	            case "DENTIST" -> "Dentist";
	            case "RECEPTIONIST" -> "Receptionist";
	            case "ASSISTANT" -> "Dental Assistant";
	            default -> role;
	        };
	    }

	    public Long getId() { return id; }
	    public Long getUserId() { return userId; }
	    public Long getBranchId() { return branchId; }
	    public String getFirstName() { return firstName; }
	    public String getMiddleName() { return middleName; }
	    public String getLastName() { return lastName; }
	    public String getEmail() { return email; }
	    public String getPhoneNumber() { return phoneNumber; }
	    public String getRole() { return role; }
	    public String getGender() { return gender; }
	    public LocalDate getDateOfBirth() { return dateOfBirth; }
	    public String getAddress() { return address; }
	    public Boolean getIsActive() { return isActive; }
	    public LocalDateTime getCreatedAt() { return createdAt; }
	    public LocalDateTime getUpdatedAt() { return updatedAt; }
	    public UserModel getUserModel() { return userModel; }

	    public void setId(Long id) { this.id = id; }
	    public void setUserId(Long userId) { this.userId = userId; }
	    public void setBranchId(Long branchId) { this.branchId = branchId; }
	    public void setFirstName(String firstName) { this.firstName = firstName; }
	    public void setMiddleName(String middleName) { this.middleName = middleName; }
	    public void setLastName(String lastName) { this.lastName = lastName; }
	    public void setEmail(String email) { this.email = email; }
	    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	    public void setRole(String role) { this.role = role; }
	    public void setGender(String gender) { this.gender = gender; }
	    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
	    public void setAddress(String address) { this.address = address; }
	    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
	    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	    public void setUserModel(UserModel userModel) { this.userModel = userModel; }

}
