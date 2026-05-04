package com.mobileApplication.dto;

import java.time.LocalDateTime;

public class SlotDTO {

    // Raw data (used internally)
    private Long slotRecommendationId;
    private Long dentistId;
    private Long serviceId;
    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;

    // Display data (mapped to RecommendedSlotScreen fields)
    private String dentistName;
    private String serviceName;
    private String month;       // "April"
    private String day;         // "9"
    private String dayOfWeek;   // "Monday"
    private String timeRange;   // "2:00 PM - 2:45 PM"
    private String duration;    // "45 min"

    public SlotDTO() {}

    // Internal constructor (used by SlotGeneratorService before dentist name is resolved)
    public SlotDTO(Long dentistId, Long serviceId, LocalDateTime slotStart, LocalDateTime slotEnd) {
        this.dentistId = dentistId;
        this.serviceId = serviceId;
        this.slotStart = slotStart;
        this.slotEnd = slotEnd;
    }

	public Long getSlotRecommendationId() {
		return slotRecommendationId;
	}

	public void setSlotRecommendationId(Long slotRecommendationId) {
		this.slotRecommendationId = slotRecommendationId;
	}

	public Long getDentistId() {
		return dentistId;
	}

	public void setDentistId(Long dentistId) {
		this.dentistId = dentistId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public LocalDateTime getSlotStart() {
		return slotStart;
	}

	public void setSlotStart(LocalDateTime slotStart) {
		this.slotStart = slotStart;
	}

	public LocalDateTime getSlotEnd() {
		return slotEnd;
	}

	public void setSlotEnd(LocalDateTime slotEnd) {
		this.slotEnd = slotEnd;
	}

	public String getDentistName() {
		return dentistName;
	}

	public void setDentistName(String dentistName) {
		this.dentistName = dentistName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}