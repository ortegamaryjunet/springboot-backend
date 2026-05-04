package com.mobileApplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.UserInfo;

public interface MobileUserInfoRepository extends JpaRepository<UserInfo, Long> {
	
	Optional<UserInfo> findByEmail(String email);
	boolean existsByEmail (String email);
	
	Optional<UserInfo> findByUsername(String username);
}
