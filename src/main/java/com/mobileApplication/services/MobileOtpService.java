package com.mobileApplication.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mobileApplication.dto.EmailRequest;
import com.mobileApplication.models.OtpModel;
import com.mobileApplication.models.UserModel;
import com.mobileApplication.repositories.MobileOtpRepository;

@Service
public class MobileOtpService {
	
	private final MobileOtpRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final WebClient webClient;
	
	@Value("${resend.api.key}")
	private String resendApiKey;
	
	public MobileOtpService(MobileOtpRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.webClient = WebClient.builder()
				.baseUrl("https://api.resend.com")
				.build();
	}
	
	public void sendOtp(UserModel user) {
		
		String rawOtp = String.format("%06d", new Random().nextInt(999999));
		
		OtpModel otp = new OtpModel();
		otp.setUserId(user.getId());
		otp.setOtp(passwordEncoder.encode(rawOtp));
		otp.setCreatedAt(LocalDateTime.now());
		otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
		otp.setVerified(false);
		
		repository.save(otp);
		
		webClient.post()
		.uri("/emails")
		.header("Authorization", "Bearer " + resendApiKey)
        .bodyValue(new EmailRequest("Smile Empress Dental Hub <noreply@mail.smileempressdentalhub.com>", List.of(user.getEmail()), "Your OTP", "Your OTP is: " + rawOtp))
        .retrieve()
        .bodyToMono(String.class)
        .subscribe();
	}
	
	public boolean verifyOtp(UserModel user, String rawOtp) {
		
		var otpOpt = repository.findTopByUserAndVerifiedFalseOrderByExpiresAtDesc(user);
		
		if (otpOpt.isEmpty()) return false;
		
		OtpModel otp = otpOpt.get();
		
		if (otp.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        if (!passwordEncoder.matches(rawOtp, otp.getOtp())) return false;

        otp.setVerified(true);
        repository.save(otp);

        return true;
		
	}
	
	public boolean hasRecentlyVerifiedOtp(UserModel user) {
	    return repository
	        .findTopByUserAndVerifiedTrueOrderByExpiresAtDesc(user)
	        .map(otp -> otp.getExpiresAt().isAfter(LocalDateTime.now()))
	        .orElse(false);
	}
	
}
