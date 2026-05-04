package com.mobileApplication.controllers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
	
	@GetMapping("/me")
	public Map<String, Object> getProfile() {
		String username = (String) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		return Map.of(
				"username", username,
				"status", "active",
				"timestamp", LocalDateTime.now().toString(),
				"message", "Profile accessed with JWT");
	}
}
