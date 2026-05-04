package com.mobileApplication.repositories.web;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.web.AdminEmployeeInfo;

public interface AdminEmployeeInfoRepository extends JpaRepository<AdminEmployeeInfo, Long> {
	
	List<AdminEmployeeInfo> findByRoleIn(List<String> roles);
	
	long countByRoleIgnoreCase(String role);
	
	Optional<AdminEmployeeInfo> findByUserId(Long userId);

}
