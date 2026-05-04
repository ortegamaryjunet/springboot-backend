package com.mobileApplication.dto;

public record RegisterRequest (
		String firstName, 
		String middleName, 
		String lastName,
		String username, 
		String password, 
		String email) {
}
