package com.mobileApplication.repositories.web;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.Dentist;

public interface RecepDentist extends JpaRepository<Dentist, Long> {
	
	long countByIsActive(boolean isActive);

}
