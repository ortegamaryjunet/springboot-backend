package com.mobileApplication.services.web;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobileApplication.models.UserModel;
import com.mobileApplication.models.web.WebOtpModel;
import com.mobileApplication.repositories.web.WebOtpRepository;

@Service
public class WebOtpService {
	
	private final WebOtpRepository webOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminResendEmailService resendEmailService;

    public WebOtpService(
            WebOtpRepository webOtpRepository,
            PasswordEncoder passwordEncoder,
            AdminResendEmailService resendEmailService
    ) {
        this.webOtpRepository = webOtpRepository;
        this.passwordEncoder = passwordEncoder;
        this.resendEmailService = resendEmailService;
    }

    public void sendRegistrationOtp(String email, String role) {
        String rawOtp = generateOtp();

        WebOtpModel otp = new WebOtpModel();
        otp.setEmail(email);
        otp.setUserId(null);
        otp.setOtp(passwordEncoder.encode(rawOtp));
        otp.setPurpose("REGISTER");
        otp.setRole(role);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);

        webOtpRepository.save(otp);

        resendEmailService.sendOtpEmail(email, rawOtp, "REGISTER");
    }

    public void sendForgotPasswordOtp(UserModel user) {
        String rawOtp = generateOtp();

        WebOtpModel otp = new WebOtpModel();
        otp.setEmail(user.getEmail());
        otp.setUserId(user.getId());
        otp.setOtp(passwordEncoder.encode(rawOtp));
        otp.setPurpose("FORGOT_PASSWORD");
        otp.setRole(user.getRole());
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);

        webOtpRepository.save(otp);

        resendEmailService.sendOtpEmail(user.getEmail(), rawOtp, "FORGOT_PASSWORD");
    }

    public boolean verifyRegistrationOtp(String email, String rawOtp) {
        var otpOpt = webOtpRepository
                .findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(email, "REGISTER");

        if (otpOpt.isEmpty()) {
            return false;
        }

        WebOtpModel otp = otpOpt.get();

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (!passwordEncoder.matches(rawOtp, otp.getOtp())) {
            return false;
        }

        otp.setVerified(true);
        webOtpRepository.save(otp);

        return true;
    }

    public boolean verifyForgotPasswordOtp(UserModel user, String rawOtp) {
        var otpOpt = webOtpRepository
                .findTopByUserIdAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
                        user.getId(),
                        "FORGOT_PASSWORD"
                );

        if (otpOpt.isEmpty()) {
            return false;
        }

        WebOtpModel otp = otpOpt.get();

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (!passwordEncoder.matches(rawOtp, otp.getOtp())) {
            return false;
        }

        otp.setVerified(true);
        webOtpRepository.save(otp);

        return true;
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    
    public boolean resendOtp(String email, String purpose) {

        var otpOpt = webOtpRepository
                .findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(email, purpose);

        if (otpOpt.isEmpty()) return false;

        WebOtpModel otp = otpOpt.get();

        int resendCount = otp.getResendCount() == null ? 0 : otp.getResendCount();

        if (resendCount >= 3) {
            return false; // limit reached
        }

        String rawOtp = generateOtp();

        otp.setOtp(passwordEncoder.encode(rawOtp));
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setResendCount(resendCount + 1);
        otp.setLastSentAt(LocalDateTime.now());

        webOtpRepository.save(otp);

        resendEmailService.sendOtpEmail(email, rawOtp, purpose);

        return true;
    }

}
