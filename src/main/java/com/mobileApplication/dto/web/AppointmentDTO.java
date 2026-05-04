package com.mobileApplication.dto.web;

public class AppointmentDTO {
	
	private Long   id;
    private String patientFullName;   // resolved from users_info
    private String serviceName;       // resolved from services_table (or "N/A")
    private String scheduledStart;    // pre-formatted "hh:mm a" string (e.g. "09:30 AM")
    private String scheduledEnd;      // pre-formatted "hh:mm a" string
    private String scheduledDate;     // pre-formatted "MMM dd, yyyy" string (e.g. "May 04, 2026")
    private String status;
    private String bookingType;
    private String urgencyLevel;
    private String patientNotes;
    private Long   dentistId;
    private Long   userInfoId;

    public AppointmentDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientFullName() { return patientFullName; }
    public void setPatientFullName(String patientFullName) { this.patientFullName = patientFullName; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getScheduledStart() { return scheduledStart; }
    public void setScheduledStart(String scheduledStart) { this.scheduledStart = scheduledStart; }

    public String getScheduledEnd() { return scheduledEnd; }
    public void setScheduledEnd(String scheduledEnd) { this.scheduledEnd = scheduledEnd; }

    public String getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }

    public String getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public String getPatientNotes() { return patientNotes; }
    public void setPatientNotes(String patientNotes) { this.patientNotes = patientNotes; }

    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }

    public Long getUserInfoId() { return userInfoId; }
    public void setUserInfoId(Long userInfoId) { this.userInfoId = userInfoId; }

}
