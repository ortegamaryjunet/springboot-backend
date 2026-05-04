package com.mobileApplication.controllers;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobileApplication.dto.LoginRequest;
import com.mobileApplication.dto.LoginResponse;
import com.mobileApplication.dto.RegisterRequest;
import com.mobileApplication.dto.UpdateProfileRequest;
import com.mobileApplication.models.UserInfo;
import com.mobileApplication.models.UserModel;
import com.mobileApplication.services.MobileLoginService;
import com.mobileApplication.services.MobileOtpService;
import com.mobileApplication.services.MobileRegistrationService;
import com.mobileApplication.services.MobileUserService;
import com.mobileApplication.utils.JwUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final MobileLoginService mobileLoginService;
	private final MobileOtpService mobileOtpService;
	private final MobileUserService mobileUserService;
	private final MobileRegistrationService registrationService; 
	private final JwUtil jwUtil;
	
	public AuthController (MobileLoginService mobileLoginService, MobileOtpService mobileOtpService, MobileUserService mobileUserService, MobileRegistrationService registrationService, JwUtil jwUtil)
	{
		this.mobileLoginService = mobileLoginService;
		this.mobileOtpService = mobileOtpService;
		this.mobileUserService = mobileUserService;
		this.registrationService = registrationService;
		this.jwUtil = jwUtil;
	}

	@GetMapping("/")
public String home() {
    return "ClinicRole"; // MUST match ClinicRole.html (no .html)
}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		
		if (request.username() == null || request.username().trim().isEmpty() ||
		        request.password() == null || request.password().trim().isEmpty()) 
		{
			return ResponseEntity.badRequest()
					.body(Map.of("status", "MISSING_CREDENTIALS", "message", "Username and password required"));
		}
		
		LoginResponse response = mobileLoginService.login(request.username(), request.password());
		
		if ("SUCCESS".equals(response.status()))
		{
			try {
				mobileLoginService.updateUserStatusOnFirstLogin(request.username());
			} catch (Exception e) {
				log.error("Failed to update user status for {}: {}", request.username(), e.getMessage());
			}
			
			return ResponseEntity.ok(Map.of(
					"status", response.status(),
					"token", response.token(),
					"user", response.user()));
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("status", response.status()));
	}

	//changing password
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
		
		String username = body.get("username");
		String newPassword = body.get("newPassword");
		
		if (username == null || username.trim().isEmpty() ||
		        newPassword == null || newPassword.trim().isEmpty()) {
		        return ResponseEntity.badRequest()
		                .body(Map.of("status", "MISSING_FIELDS"));
		    }
		
		var userOpt = mobileUserService.findByUsername(username);
		if (userOpt.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("status", "USER_NOT_FOUND"));
		}
		
		boolean otpVerified = mobileOtpService.hasRecentlyVerifiedOtp(userOpt.get());
	    if (!otpVerified) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("status", "OTP_NOT_VERIFIED"));
	    }
		
		mobileUserService.updatePassword(userOpt.get(), newPassword);
		
		return ResponseEntity.ok(Map.of("status", "PASSWORD_RESET_SUCCESS"));
	}
	
	//for development/testing only
	@GetMapping("/debug")
	public Map<String, Object> debug(HttpServletRequest request) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    
	    return Map.of(
	        "timestamp", Instant.now(),
	        "uri", request.getRequestURI(),
	        "method", request.getMethod(),
	        "auth_header_raw", request.getHeader("Authorization") != null ? request.getHeader("Authorization") : null,
	        "auth_header_present", request.getHeader("Authorization") != null,
	        "filter_fired", "FILTER PASSED",
	        "auth_object", auth != null ? auth.getPrincipal().toString() : "NULL",
	        "is_authenticated", auth != null && auth.isAuthenticated(),
	        "authorities", auth != null ? auth.getAuthorities().toString() : "NONE",
	        "is_anonymous", auth != null && "anonymousUser".equals(auth.getPrincipal())
	    );
	}
	
	//send otp
	@PostMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
		
		String username = body.get("username");
		
		if (!body.containsKey("username") || body.get("username") == null || body.get("username").trim().isEmpty()) {
	        return ResponseEntity.badRequest().body(Map.of("status", "MISSING_USERNAME"));
		}
		
		var userOpt = mobileUserService.findByUsername(username);
		if (userOpt.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("status", "USER_NOT_FOUND"));
		}
		
		mobileOtpService.sendOtp(userOpt.get());
		
		return ResponseEntity.ok(Map.of(
				"status", "OTP_SENT",
				"message", "OTP sent to registered email"));
	}
	
	//otp verification
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
		
		String username = body.get("username");
		String otp = body.get("otp");
		
		if (username == null || username.trim().isEmpty() ||
		        otp == null || otp.trim().isEmpty()) {
		        return ResponseEntity.badRequest()
		                .body(Map.of("status", "MISSING_FIELDS"));
		    }
		
		var userOpt = mobileUserService.findByUsername(username);
		if (userOpt.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("status", "USER_NOT_FOUND"));
		}
		boolean isValid = mobileOtpService.verifyOtp(userOpt.get(), otp);
		
		if(!isValid)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("status", "INVALID_OTP"));
		}
		
		UserModel user = userOpt.get();
		if ("PENDING".equals(user.getStatus())) {
		    user.setStatus("ACTIVE");
		    mobileUserService.saveUser(user);
		}
		
		return ResponseEntity.ok(Map.of("status", "OTP_VERIFIED"));
	}
	
	//register
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		
		if (request.firstName() == null || request.firstName().trim().isEmpty() ||
			request.middleName() == null || request.middleName().trim().isEmpty() ||
			request.lastName() == null || request.lastName().trim().isEmpty() ||
			request.username() == null || request.username().trim().isEmpty() ||
		    request.password() == null || request.password().trim().isEmpty() ||
		    request.email()    == null || request.email().trim().isEmpty()) {
		    return ResponseEntity.badRequest()
		                .body(Map.of("status", "MISSING_FIELDS"));
		    }

		    try {
		        UserModel user = registrationService.register(
		            request.firstName(), request.middleName(), 
		            request.lastName(),request.username(), 
		            request.password(), request.email()
		        );

		        mobileOtpService.sendOtp(user);

		        return ResponseEntity.ok(Map.of(
		            "status", "REGISTERED",
		            "message", "Registration successful. OTP sent to your email."
		        ));

		    } catch (RuntimeException e) {
		        return ResponseEntity.badRequest()
		                .body(Map.of("status", "REGISTRATION_FAILED", "message", e.getMessage()));
		    }
	}
	
	//profile
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("status", "UNAUTHORIZED"));
	    }

	    String token = authHeader.substring(7);
	    String username;

	    try {
	        username = jwUtil.extractUsername(token);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	        		.body(Map.of("status", "INVALID_TOKEN"));
	    }

	    if (username == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("status", "INVALID_TOKEN"));
	    }

	    var userOpt = mobileUserService.findByUsername(username);
	    if (userOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("status", "USER_NOT_FOUND"));
	    }

	    UserModel user = userOpt.get();

	    var userInfoOpt = mobileUserService.findUserInfoByUsername(username);
	    if (userInfoOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("status", "USER_INFO_NOT_FOUND"));
	    }

	    var info = userInfoOpt.get();

	    return ResponseEntity.ok(Map.of(
	            "firstName",   info.getFirstName()   != null ? info.getFirstName()   : "",
	            "middleName",  info.getMiddleName()  != null ? info.getMiddleName()  : "",
	            "lastName",    info.getLastName()    != null ? info.getLastName()    : "",
	            "email",       info.getEmail()       != null ? info.getEmail()       : "",
	            "phoneNumber", info.getPhoneNumber() != null ? info.getPhoneNumber() : "",
	            "dateOfBirth", info.getDateOfBirth() != null ? info.getDateOfBirth() : "",
	            "gender",      info.getGender()      != null ? info.getGender()      : "",
	            "createdAt",   user.getCreatedAt()   != null ? user.getCreatedAt().toString() : ""
	    ));
	}
	
	// Update Profile
	@PutMapping("/update/profile")
	public ResponseEntity<?>updateProfile (HttpServletRequest request, @RequestBody UpdateProfileRequest updateRequest) {
		
		String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("status", "UNAUTHORIZED"));
		}
		
		String token = authHeader.substring(7);
		String username;
		
		try {
			username = jwUtil.extractUsername(token);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("status", "INVALID_TOKEN"));
		}
		
		if (username == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("status", "INVALID_TOKEN"));
		}
		
		try {
			UserInfo updated = mobileUserService.updateUserInfo(username, updateRequest);
			
			return ResponseEntity.ok(Map.of(
					"status", "PROFILE_UPDATED",
					"phoneNumber", updated.getPhoneNumber() != null ? updated.getPhoneNumber() : "",
					"gender", updated.getGender() != null ? updated.getGender() : "",
					"dateOfBirth", updated.getDateOfBirth() != null ? updated.getDateOfBirth() : "",
					"address", updated.getAddress() != null ? updated.getAddress() : "", 
					"email", updated.getEmail() != null ? updated.getEmail() : ""
					));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("status", "UPDATE_FAILED", "message", e.getMessage()));
		}
	}
	

}
