package com.mobileApplication.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobileApplication.models.Dentist;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {
	
	List<Dentist> findBySpecializationAndIsActiveTrue(String specialization);
    List<Dentist> findByBranchIdAndIsActiveTrue(Long branchId);
    
}
