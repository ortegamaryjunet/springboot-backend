package com.mobileApplication.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileApplication.dto.BranchDTO;
import com.mobileApplication.dto.DentistDTO;
import com.mobileApplication.dto.ServiceDTO;
import com.mobileApplication.models.Dentist;
import com.mobileApplication.models.DentistSchedule;
import com.mobileApplication.models.Services;
import com.mobileApplication.repositories.BranchRepository;
import com.mobileApplication.repositories.DentistRepository;
import com.mobileApplication.repositories.DentistScheduleRepository;
import com.mobileApplication.repositories.ServicesRepository;

@Service
public class ClinicService {

	@Autowired
    private BranchRepository branchRepository;

    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private DentistScheduleRepository dentistScheduleRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    private static final String[] DAY_NAMES = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };

    public List<BranchDTO> getAllBranches() {
        return branchRepository.findAll().stream()
        		.map(b -> new BranchDTO(
        				b.getId(),
        	            b.getBranchName(),
        	            b.getBranchAddress(),
        	            b.getOperatingHours(),
        	            b.getStatus(),
        	            b.getYearsOpen()
        	        ))
        	        .collect(Collectors.toList());
    }

    public List<DentistDTO> getAllDentists() {
        return dentistRepository.findAll().stream()
            .filter(Dentist::isActive)
            .map(d -> {
                List<DentistSchedule> schedules = dentistScheduleRepository.findByDentistId(d.getId());
                List<String> days = schedules.stream()
                    .filter(DentistSchedule::isAvailable)
                    .map(s -> DAY_NAMES[s.getDayOfWeek()])
                    .collect(Collectors.toList());
                return new DentistDTO(d.getId(), d.getFirstname(), d.getSurname(), d.getSpecialization(), days);
            })
            .collect(Collectors.toList());
    }

    public List<ServiceDTO> getAllServices() {
        return servicesRepository.findAll().stream()
            .filter(Services::isActive)
            .map(s -> new ServiceDTO(s.getId(), s.getName(), s.getDescription(), s.getRequiredSpecialization(), s.getEstimatedDurationMin()))
            .collect(Collectors.toList());
    }

}
