package com.mobileApplication.repositories.web;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobileApplication.models.Dentist;

@Repository
public interface DentistRepo extends JpaRepository<Dentist, Long> {
	
	Optional<Dentist> findByEmail(String email);

}
