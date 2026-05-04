package com.mobileApplication.repositories.web;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.web.RecepMessage;

public interface RecepMessageRepository extends JpaRepository<RecepMessage, Long> {

	List<RecepMessage> findByPatientIdOrderByCreatedAtAsc(Long patientId);
}
