package com.mobileApplication.dto;

import java.util.List;

public class RiskAssessmentRequest {
    private Long userId;

    // Q1 — current symptoms (indices mapped to labels on backend)
    private List<String> currentSymptoms;    // e.g. ["Tooth pain or sensitivity", "Bleeding gums"]
    private boolean symptomsNone;

    // Q2 — brush frequency
    private String brushFrequency;           // e.g. "Twice a day or more"

    // Q3 — last dental visit
    private String lastDentalVisit;          // e.g. "Within 6 months"

    // Q4 — daily habits
    private List<String> dailyHabits;        // e.g. ["Eat sugary foods/drinks often"]
    private boolean habitsNone;

    // Q5 — health conditions
    private List<String> healthConditions;   // e.g. ["Diabetes"]
    private boolean conditionsNone;

    // Q6 — hygiene sub-questions
    private String flossFrequency;           // "Daily" | "Sometimes" | "Never"
    private String mouthwashFrequency;       // "Daily" | "Sometimes" | "Never"
    private String smokingStatus;            // "Yes, currently" | "Former smoker" | "Never"

    // Q7 — optional notes
    private String additionalNotes;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getCurrentSymptoms() {
		return currentSymptoms;
	}

	public void setCurrentSymptoms(List<String> currentSymptoms) {
		this.currentSymptoms = currentSymptoms;
	}

	public boolean isSymptomsNone() {
		return symptomsNone;
	}

	public void setSymptomsNone(boolean symptomsNone) {
		this.symptomsNone = symptomsNone;
	}

	public String getBrushFrequency() {
		return brushFrequency;
	}

	public void setBrushFrequency(String brushFrequency) {
		this.brushFrequency = brushFrequency;
	}

	public String getLastDentalVisit() {
		return lastDentalVisit;
	}

	public void setLastDentalVisit(String lastDentalVisit) {
		this.lastDentalVisit = lastDentalVisit;
	}

	public List<String> getDailyHabits() {
		return dailyHabits;
	}

	public void setDailyHabits(List<String> dailyHabits) {
		this.dailyHabits = dailyHabits;
	}

	public boolean isHabitsNone() {
		return habitsNone;
	}

	public void setHabitsNone(boolean habitsNone) {
		this.habitsNone = habitsNone;
	}

	public List<String> getHealthConditions() {
		return healthConditions;
	}

	public void setHealthConditions(List<String> healthConditions) {
		this.healthConditions = healthConditions;
	}

	public boolean isConditionsNone() {
		return conditionsNone;
	}

	public void setConditionsNone(boolean conditionsNone) {
		this.conditionsNone = conditionsNone;
	}

	public String getFlossFrequency() {
		return flossFrequency;
	}

	public void setFlossFrequency(String flossFrequency) {
		this.flossFrequency = flossFrequency;
	}

	public String getMouthwashFrequency() {
		return mouthwashFrequency;
	}

	public void setMouthwashFrequency(String mouthwashFrequency) {
		this.mouthwashFrequency = mouthwashFrequency;
	}

	public String getSmokingStatus() {
		return smokingStatus;
	}

	public void setSmokingStatus(String smokingStatus) {
		this.smokingStatus = smokingStatus;
	}

	public String getAdditionalNotes() {
		return additionalNotes;
	}

	public void setAdditionalNotes(String additionalNotes) {
		this.additionalNotes = additionalNotes;
	}
     
}
