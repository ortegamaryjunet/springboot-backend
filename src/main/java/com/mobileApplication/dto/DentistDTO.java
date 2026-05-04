package com.mobileApplication.dto;

import java.util.List;

public class DentistDTO {
	
    private Long id;
    private String firstname;
    private String surname;
    private String specialization;
    private List<String> availableDays;

    public DentistDTO() {}

    public DentistDTO(Long id, String firstname, String surname, String specialization, List<String> availableDays) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.specialization = specialization;
        this.availableDays = availableDays;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public List<String> getAvailableDays() { return availableDays; }
    public void setAvailableDays(List<String> availableDays) { this.availableDays = availableDays; }
}
