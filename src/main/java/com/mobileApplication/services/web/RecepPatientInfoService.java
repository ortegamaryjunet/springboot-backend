package com.mobileApplication.services.web;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mobileApplication.models.UserInfo;
import com.mobileApplication.repositories.web.RecepUserInfo;

@Service
public class RecepPatientInfoService {
	
	private final RecepUserInfo patientRepository;

    public RecepPatientInfoService(RecepUserInfo patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<UserInfo> getAllPatients() {
        return patientRepository.findAll();
    }
    
    public void updatePatientInfo(
            Long infoId,
            String firstName,
            String middleName,
            String lastName,
            String dateOfBirth,
            String gender,
            String email,
            String phoneNumber,
            String address
    ) {
        UserInfo info = patientRepository.findById(infoId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        info.setFirstName(firstName);
        info.setMiddleName(middleName);
        info.setLastName(lastName);
        info.setDateOfBirth(dateOfBirth);
        info.setGender(gender);
        info.setEmail(email);
        info.setPhoneNumber(phoneNumber);
        info.setAddress(address);

        patientRepository.save(info);
    }
    
    
    

}
