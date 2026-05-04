package com.mobileApplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.OtpModel;
import com.mobileApplication.models.UserModel;

public interface MobileOtpRepository extends JpaRepository<OtpModel, Long> {
	
	Optional<OtpModel> findTopByUserAndVerifiedFalseOrderByExpiresAtDesc(UserModel user);
	Optional<OtpModel> findTopByUserAndVerifiedTrueOrderByExpiresAtDesc(UserModel user);

}
