package com.mobileApplication.services.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobileApplication.models.UserInfo;
import com.mobileApplication.models.UserModel;
import com.mobileApplication.repositories.web.RecepUser;
import com.mobileApplication.repositories.web.RecepUserInfo;

@Service
public class RecepPatientAccountService {
	
    private final RecepUserInfo userInfoRepository;
    private final RecepUser userRepository;
    private final PasswordEncoder passwordEncoder;

    public RecepPatientAccountService(
    		RecepUserInfo userInfoRepository,
    		RecepUser userRepository,
    		PasswordEncoder passwordEncoder
    ) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserInfo> getAllPatientAccounts() {
        return userInfoRepository.findAll();
    }

    public long getTotalPatientAccounts() {
        return userInfoRepository.count();
    }

    public long getActivePatients() {
        return userRepository.countByStatusIgnoreCase("ACTIVE");
    }

    public long getInactivePatients() {
        return userRepository.countByStatusIgnoreCase("INACTIVE");
    }
    
    public void updatePatientAccount(
            Long infoId,
            Long userId,
            String firstName,
            String middleName,
            String lastName,
            String username,
            String status
    ) 
    {
        UserInfo info = userInfoRepository.findById(infoId)
                .orElseThrow(() -> new RuntimeException("Patient info not found"));

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User account not found"));

        info.setFirstName(firstName);
        info.setMiddleName(middleName);
        info.setLastName(lastName);
        info.setUsername(username);

        user.setUsername(username);

        if ("INACTIVE".equalsIgnoreCase(status)) {
            user.setStatus("INACTIVE");
            user.setDeactivatedAt(LocalDateTime.now());
        } else {
            user.setStatus("ACTIVE");
            user.setDeactivatedAt(null);
        }

        userInfoRepository.save(info);
        userRepository.save(user);
    }
    
    public void createPatientAccount(
            String firstName,
            String middleName,
            String lastName,
            String email,
            String username,
            String password
    ) {
        StringBuilder errors = new StringBuilder();

        if (userRepository.existsByUsername(username)) {
            errors.append("Username already exists. ");
        }

        if (userRepository.existsByEmail(email)) {
            errors.append("Email already exists.");
        }

        if (errors.length() > 0) {
            throw new RuntimeException(errors.toString().trim());
        }

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setEmail(email);
        user.setHashedPassword(passwordEncoder.encode(password));
        user.setStatus("ACTIVE");

        UserModel savedUser = userRepository.save(user);

        UserInfo info = new UserInfo();
        info.setFirstName(firstName);
        info.setMiddleName(middleName);
        info.setLastName(lastName);
        info.setEmail(email);
        info.setUsername(username);
        info.setUserModel(savedUser);

        userInfoRepository.save(info);
    }
    
}
