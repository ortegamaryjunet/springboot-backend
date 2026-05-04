package com.mobileApplication.services;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobileApplication.dto.UpdateProfileRequest;
import com.mobileApplication.models.UserInfo;
import com.mobileApplication.models.UserModel;
import com.mobileApplication.repositories.MobileUserInfoRepository;
import com.mobileApplication.repositories.MobileUserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MobileUserService {
	
	private final MobileUserRepository mobileUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final MobileUserInfoRepository mobileUserInfoRepository;
	
	public MobileUserService(MobileUserRepository mobileUserRepository, PasswordEncoder passwordEncoder, MobileUserInfoRepository mobileUserInfoRepository) {
		this.mobileUserRepository = mobileUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.mobileUserInfoRepository = mobileUserInfoRepository;
	}
	
	public Optional<UserModel> findByUsername(String username) {
		return mobileUserRepository.findByUsername(username);
	}
	
	public void updatePassword(UserModel user, String newPassword) {
		user.setHashedPassword(passwordEncoder.encode(newPassword));
		user.setPasswordChangeCount(user.getPasswordChangeCount() +1);
		mobileUserRepository.save(user);
	}
	
	public void saveUser(UserModel user) {
	    mobileUserRepository.save(user);
	}
	
	public Optional<UserInfo> findUserInfoByUsername(String username) {
		return mobileUserInfoRepository.findByUsername(username);
	}
	
	public UserInfo updateUserInfo(String username, UpdateProfileRequest request) {
		UserInfo info = mobileUserInfoRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User info not found."));
		
		if (request.phoneNumber() != null && !request.phoneNumber().isBlank())
			info.setPhoneNumber(request.phoneNumber());
		
		if (request.gender() != null && !request.gender().isBlank())
			info.setGender(request.gender());
		
	    if (request.dateOfBirth() != null && !request.dateOfBirth().isBlank())
	        info.setDateOfBirth(request.dateOfBirth());
	
	    if (request.address() != null && !request.address().isBlank())
	        info.setAddress(request.address());

	    if (request.email() != null && !request.email().isBlank())
	        info.setEmail(request.email());
	    
	    return mobileUserInfoRepository.save(info);
	}
	
}
