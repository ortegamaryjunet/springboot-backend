package com.mobileApplication.repositories.web;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.UserModel;

public interface AdminRegUserRepository extends JpaRepository<UserModel, Long> {
	
	Optional<UserModel> findByEmail(String email);
	
	Optional<UserModel> findByEmailAndRole(String email, String role);
	boolean existsByEmail(String email);

}
