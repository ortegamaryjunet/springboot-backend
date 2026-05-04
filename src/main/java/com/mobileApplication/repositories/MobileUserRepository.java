package com.mobileApplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.UserModel;

public interface MobileUserRepository extends JpaRepository<UserModel, Long> {
	
	Optional<UserModel> findByUsername(String username);
	boolean existsByUsername (String username);
	
	//for web
	Optional<UserModel> findByEmail(String email);
	
	Optional<UserModel> findByEmailAndRole(String email, String role);

}
