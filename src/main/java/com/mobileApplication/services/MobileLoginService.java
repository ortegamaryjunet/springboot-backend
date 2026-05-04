package com.mobileApplication.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobileApplication.dto.LoginResponse;
import com.mobileApplication.dto.UserInfo;
import com.mobileApplication.repositories.MobileUserRepository;
import com.mobileApplication.utils.JwUtil;

import jakarta.transaction.Transactional;

@Service
public class MobileLoginService {
	
	private final MobileUserRepository mobileUserRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwUtil jwUtil;
	
	public MobileLoginService(MobileUserRepository mobileUserRepository, BCryptPasswordEncoder passwordEncoder, JwUtil jwUtil)
	{
		this.mobileUserRepository = mobileUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwUtil = jwUtil;
	}
	
	//login
	public LoginResponse login(String username, String password) {
		
		return mobileUserRepository.findByUsername(username)
				.filter(user -> passwordEncoder.matches(password, user.getHashedPassword()))
				.map(user -> {
					
					if ("PENDING".equals(user.getStatus())) {
		                return new LoginResponse("ACCOUNT_NOT_VERIFIED", null, null);
		            }
					
					String token = jwUtil.generateToken(user);
					return new LoginResponse("SUCCESS", token, new UserInfo(user.getId(), 
							user.getUsername(),
							user.getUsername(),
							user.getBranch() != null ? user.getBranch().getBranchName() : "N/A",
							user.getBranchId() 
							)
						);
				})
				.orElse(new LoginResponse("INVALID_CREDENTIALS", null, null));
	}
	
	@Transactional
	public void updateUserStatusOnFirstLogin(String username) {
		mobileUserRepository.findByUsername(username)
		.filter(user -> !"ACTIVE".equals(user.getStatus()))
		.ifPresent(user -> {
			user.setStatus("ACTIVE");
			mobileUserRepository.save(user);
		});
	}

}
