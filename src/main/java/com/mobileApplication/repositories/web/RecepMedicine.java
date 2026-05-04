package com.mobileApplication.repositories.web;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.web.RecepDentalMedicine;

public interface RecepMedicine extends JpaRepository<RecepDentalMedicine, Long> {

}
