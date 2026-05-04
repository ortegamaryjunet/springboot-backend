package com.mobileApplication.repositories.web;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.web.WebOtpModel;

public interface WebOtpRepository extends JpaRepository<WebOtpModel, Long> {
	
	Optional<WebOtpModel> findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
            String email,
            String purpose
    );

    Optional<WebOtpModel> findTopByUserIdAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
            Long userId,
            String purpose
    );

}
