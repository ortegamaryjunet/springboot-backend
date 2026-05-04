package com.mobileApplication.dto.web;

public class AppointmentRowDTO {
	
	private Long   id;
    private String patientName;
    private String dentistName;
    private String serviceName;
    private String scheduledStart;   // formatted "hh:mm a"  e.g. "10:00 AM"
    private String scheduledEnd;     // formatted "hh:mm a"
    private String dayOfWeek;        // e.g. "Wed"
    private int    dayOfMonth;       // e.g. 4
    private String status;           // pending / arrived / cancelled / walk_in
    private String bookingType;      // scheduled / walk_in
    private String urgencyLevel;     // low / medium / high / emergency
    private String patientNotes;

    public AppointmentRowDTO() {}

    public AppointmentRowDTO(Long id, String patientName, String dentistName,
                             String serviceName, String scheduledStart, String scheduledEnd,
                             String dayOfWeek, int dayOfMonth,
                             String status, String bookingType,
                             String urgencyLevel, String patientNotes) {
        this.id             = id;
        this.patientName    = patientName;
        this.dentistName    = dentistName;
        this.serviceName    = serviceName;
        this.scheduledStart = scheduledStart;
        this.scheduledEnd   = scheduledEnd;
        this.dayOfWeek      = dayOfWeek;
        this.dayOfMonth     = dayOfMonth;
        this.status         = status;
        this.bookingType    = bookingType;
        this.urgencyLevel   = urgencyLevel;
        this.patientNotes   = patientNotes;
    }

    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public String getPatientName()                   { return patientName; }
    public void   setPatientName(String patientName) { this.patientName = patientName; }

    public String getDentistName()                   { return dentistName; }
    public void   setDentistName(String dentistName) { this.dentistName = dentistName; }

    public String getServiceName()                   { return serviceName; }
    public void   setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getScheduledStart()                        { return scheduledStart; }
    public void   setScheduledStart(String scheduledStart)   { this.scheduledStart = scheduledStart; }

    public String getScheduledEnd()                      { return scheduledEnd; }
    public void   setScheduledEnd(String scheduledEnd)   { this.scheduledEnd = scheduledEnd; }

    public String getDayOfWeek()                   { return dayOfWeek; }
    public void   setDayOfWeek(String dayOfWeek)   { this.dayOfWeek = dayOfWeek; }

    public int  getDayOfMonth()              { return dayOfMonth; }
    public void setDayOfMonth(int dayOfMonth){ this.dayOfMonth = dayOfMonth; }

    public String getStatus()              { return status; }
    public void   setStatus(String status) { this.status = status; }

    public String getBookingType()                   { return bookingType; }
    public void   setBookingType(String bookingType) { this.bookingType = bookingType; }

    public String getUrgencyLevel()                      { return urgencyLevel; }
    public void   setUrgencyLevel(String urgencyLevel)   { this.urgencyLevel = urgencyLevel; }

    public String getPatientNotes()                      { return patientNotes; }
    public void   setPatientNotes(String patientNotes)   { this.patientNotes = patientNotes; }

}
