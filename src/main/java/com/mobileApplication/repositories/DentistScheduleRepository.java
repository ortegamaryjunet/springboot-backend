package com.mobileApplication.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobileApplication.models.DentistSchedule;

@Repository
public interface DentistScheduleRepository extends JpaRepository<DentistSchedule, Long> {
	
	List<DentistSchedule> findByDentistIdAndDayOfWeekAndIsAvailableTrue(Long dentistId, int dayOfWeek);
	List<DentistSchedule> findByDentistId(Long dentistId);
}
