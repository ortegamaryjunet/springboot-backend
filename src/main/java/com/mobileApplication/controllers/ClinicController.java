package com.mobileApplication.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobileApplication.dto.BranchDTO;
import com.mobileApplication.dto.DentistDTO;
import com.mobileApplication.dto.ServiceDTO;
import com.mobileApplication.services.ClinicService;

@RestController
@RequestMapping("/api/clinic")
public class ClinicController {
	
    @Autowired
    private ClinicService clinicService;

    @GetMapping("/branches")
    public ResponseEntity<List<BranchDTO>> getBranches() {
        return ResponseEntity.ok(clinicService.getAllBranches());
    }

    @GetMapping("/dentists")
    public ResponseEntity<List<DentistDTO>> getDentists() {
        return ResponseEntity.ok(clinicService.getAllDentists());
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceDTO>> getServices() {
        return ResponseEntity.ok(clinicService.getAllServices());
    }

}
