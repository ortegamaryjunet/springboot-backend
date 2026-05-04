package com.mobileApplication.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobileApplication.models.RiskAssessment;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
	
	List<RiskAssessment> findByUserInfoIdOrderByAssessedAtDesc (Long userInfoId);

}
