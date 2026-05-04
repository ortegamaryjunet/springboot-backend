package com.mobileApplication.dto;

import java.time.LocalDateTime;

public class UpcomingAppointmentDTO {
	
    private Long id;
    private String serviceName;
    private String dentistName;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private int durationMinutes;
    private String status;

    public UpcomingAppointmentDTO(Long id, String serviceName, String dentistName,
                                   LocalDateTime scheduledStart, LocalDateTime scheduledEnd,
                                   int durationMinutes, String status) {
        this.id = id;
        this.serviceName = serviceName;
        this.dentistName = dentistName;
        this.scheduledStart = scheduledStart;
        this.scheduledEnd = scheduledEnd;
        this.durationMinutes = durationMinutes;
        this.status = status;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDentistName() {
		return dentistName;
	}

	public void setDentistName(String dentistName) {
		this.dentistName = dentistName;
	}

	public LocalDateTime getScheduledStart() {
		return scheduledStart;
	}

	public void setScheduledStart(LocalDateTime scheduledStart) {
		this.scheduledStart = scheduledStart;
	}

	public LocalDateTime getScheduledEnd() {
		return scheduledEnd;
	}

	public void setScheduledEnd(LocalDateTime scheduledEnd) {
		this.scheduledEnd = scheduledEnd;
	}

	public int getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
