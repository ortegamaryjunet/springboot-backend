package com.mobileApplication.dto;

public class BranchDTO {
	
    private Long id;
    private String branchName;
    private String branchAddress;
    private String operatingHours;
    private String status;
    private String yearsOpen;

    public BranchDTO(Long id, String branchName, String branchAddress, 
                     String operatingHours, String status, String yearsOpen) {
        this.id = id;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.operatingHours = operatingHours;
        this.status = status;
        this.yearsOpen = yearsOpen;
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

	public String getYearsOpen() {
		return yearsOpen;
	}

	public void setYearsOpen(String yearsOpen) {
		this.yearsOpen = yearsOpen;
	}
    
}
