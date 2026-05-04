package com.mobileApplication.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobileApplication.models.UserInfo;
import com.mobileApplication.models.UserModel;
import com.mobileApplication.repositories.MobileUserInfoRepository;
import com.mobileApplication.repositories.MobileUserRepository;

import jakarta.transaction.Transactional;

@Service
public class MobileRegistrationService {
	
	private final MobileUserRepository userRepository;
	private final MobileUserInfoRepository userInfoRepository;
	private final PasswordEncoder passwordEncoder;
	
	public MobileRegistrationService(MobileUserRepository userRepository, MobileUserInfoRepository userInfoRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userInfoRepository = userInfoRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	public UserModel register(String firstName, String middleName, String lastName, String username, String password, String email) {
		
		if(userRepository.existsByUsername(username))
		{
			throw new RuntimeException("Username already exists.");
		}
		
		if (userInfoRepository.existsByEmail(email))
		{
			throw new RuntimeException("Email already exists.");
		}
		
		UserModel user = new UserModel();
        user.setUsername(username);
        user.setHashedPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        
        UserModel savedUser = userRepository.save(user);

        // Save email in UserInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setUserModel(savedUser);
        userInfo.setUsername(savedUser.getUsername());
        userInfo.setEmail(savedUser.getEmail());
        userInfo.setFirstName(firstName);
        userInfo.setMiddleName(middleName);
        userInfo.setLastName(lastName);
        userInfoRepository.save(userInfo);

        return savedUser;
	}
}
