package com.mobileApplication.dto.web;

public class RecepWalkInRequest {
	
	private String patientName;   // free-text for walk-ins (no account needed)
    private Long   dentistId;
    private Long   serviceId;
    private String scheduledStart; // ISO datetime string "2025-05-04T10:00"
    private String patientNotes;

    public String getPatientName()                   { return patientName; }
    public void   setPatientName(String patientName) { this.patientName = patientName; }

    public Long getDentistId()               { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }

    public Long getServiceId()               { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public String getScheduledStart()                      { return scheduledStart; }
    public void   setScheduledStart(String scheduledStart) { this.scheduledStart = scheduledStart; }

    public String getPatientNotes()                      { return patientNotes; }
    public void   setPatientNotes(String patientNotes)   { this.patientNotes = patientNotes; }

}
