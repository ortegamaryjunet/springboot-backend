package com.mobileApplication.dto;

import java.util.List;

public class SymptomRequest {
	
	private Long userId;
    private int batchNumber = 1;

    // ── Section 1: Chief Complaint ───────────────────────────────────────────
    private List<String> chiefComplaints;   // e.g. ["Tooth pain", "Gum problem"]
    private String description;             // free-text patient description

    // ── Section 2: Pain ──────────────────────────────────────────────────────
    private String hasPain;                 // "Yes" | "No"
    private List<String> painTypes;         // e.g. ["Sharp / stabbing", "Throbbing"]
    private String painLevel;               // "mild" | "moderate" | "severe"
    private String bitingPain;              // e.g. "Pain when biting"

    // ── Section 3: Sensitivity ───────────────────────────────────────────────
    private List<String> sensitivityTriggers;  // e.g. ["Cold drinks / food"]
    private String sensitivityDuration;         // e.g. "Lingers for a few minutes"

    // ── Section 4: Duration & Progress ──────────────────────────────────────
    private String duration;                // e.g. "3 – 7 days ago"
    private String progress;               // e.g. "Getting worse"

    // ── Section 5: Medication ────────────────────────────────────────────────
    private String tookMedication;         // "Yes" | "No"
    private String medicationHelped;       // "Yes" | "No"
    private String medicationName;         // e.g. "Ibuprofen"

    // ── Section 6: Dental History ────────────────────────────────────────────
    private List<String> dentalHistory;    // e.g. ["Had a root canal before"]

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}

	public List<String> getChiefComplaints() {
		return chiefComplaints;
	}

	public void setChiefComplaints(List<String> chiefComplaints) {
		this.chiefComplaints = chiefComplaints;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHasPain() {
		return hasPain;
	}

	public void setHasPain(String hasPain) {
		this.hasPain = hasPain;
	}

	public List<String> getPainTypes() {
		return painTypes;
	}

	public void setPainTypes(List<String> painTypes) {
		this.painTypes = painTypes;
	}

	public String getPainLevel() {
		return painLevel;
	}

	public void setPainLevel(String painLevel) {
		this.painLevel = painLevel;
	}

	public String getBitingPain() {
		return bitingPain;
	}

	public void setBitingPain(String bitingPain) {
		this.bitingPain = bitingPain;
	}

	public List<String> getSensitivityTriggers() {
		return sensitivityTriggers;
	}

	public void setSensitivityTriggers(List<String> sensitivityTriggers) {
		this.sensitivityTriggers = sensitivityTriggers;
	}

	public String getSensitivityDuration() {
		return sensitivityDuration;
	}

	public void setSensitivityDuration(String sensitivityDuration) {
		this.sensitivityDuration = sensitivityDuration;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getTookMedication() {
		return tookMedication;
	}

	public void setTookMedication(String tookMedication) {
		this.tookMedication = tookMedication;
	}

	public String getMedicationHelped() {
		return medicationHelped;
	}

	public void setMedicationHelped(String medicationHelped) {
		this.medicationHelped = medicationHelped;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public List<String> getDentalHistory() {
		return dentalHistory;
	}

	public void setDentalHistory(List<String> dentalHistory) {
		this.dentalHistory = dentalHistory;
	}
    
}
