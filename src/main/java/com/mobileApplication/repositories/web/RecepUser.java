package com.mobileApplication.repositories.web;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.UserModel;

public interface RecepUser extends JpaRepository<UserModel, Long> {
	
	long countByStatusIgnoreCase(String status);
	
	boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
