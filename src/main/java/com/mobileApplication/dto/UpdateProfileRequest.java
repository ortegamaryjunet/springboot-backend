package com.mobileApplication.dto;

public record UpdateProfileRequest (
		String gender, 
		String dateOfBirth,
		String phoneNumber,
		String address,
		String email)
{}
