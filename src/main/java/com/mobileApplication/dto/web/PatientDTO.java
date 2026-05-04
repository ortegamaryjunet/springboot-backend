package com.mobileApplication.dto.web;

public class PatientDTO {
	
	private Long   id;               // users_info.id
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String dateOfBirth;      // stored as varchar in DB — passed through as-is
    private String gender;
    private String lastVisit;        // pre-formatted "yyyy-MM-dd" string
    private String lastTreatment;    // service name from services_table

    public PatientDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLastVisit() { return lastVisit; }
    public void setLastVisit(String lastVisit) { this.lastVisit = lastVisit; }

    public String getLastTreatment() { return lastTreatment; }
    public void setLastTreatment(String lastTreatment) { this.lastTreatment = lastTreatment; }

}
