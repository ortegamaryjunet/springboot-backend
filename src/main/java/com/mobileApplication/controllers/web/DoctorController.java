package com.mobileApplication.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mobileApplication.models.Dentist;
import com.mobileApplication.services.web.DoctorDashboardService;


@Controller
public class DoctorController {
	
	private final DoctorDashboardService doctorDashboardService;

    public DoctorController(DoctorDashboardService doctorDashboardService) {
        this.doctorDashboardService = doctorDashboardService;
    }

    // -----------------------------------------------------------------------
    // Helper: adds dentist name + specialization to every page's model
    // -----------------------------------------------------------------------
    private void addDentistProfile(Model model) {
        Dentist dentist = doctorDashboardService.getLoggedInDentist();
        if (dentist != null) {
            model.addAttribute("dentistName", dentist.getFullName());
            model.addAttribute("dentistSpecialization",
                    dentist.getSpecialization() != null ? dentist.getSpecialization() : "");
        } else {
            model.addAttribute("dentistName", "Dentist");
            model.addAttribute("dentistSpecialization", "");
        }
    }

    // -----------------------------------------------------------------------
    // DASHBOARD
    // -----------------------------------------------------------------------

    @GetMapping("/web/doctor/dashboard")
    public String doctorDashboard(Model model) {
        addDentistProfile(model);
        model.addAttribute("totalPatients",     doctorDashboardService.getTotalPatients());
        model.addAttribute("totalAppointments", doctorDashboardService.getTotalAppointments());
        model.addAttribute("newPatients",       doctorDashboardService.getNewPatients());
        model.addAttribute("returningPatients", doctorDashboardService.getReturningPatients());
        return "DoctorDashboard";
    }

    @GetMapping("/doctorDashboard")
    public String doctorDashboardLegacy(Model model) {
        return doctorDashboard(model);
    }

    // -----------------------------------------------------------------------
    // APPOINTMENTS
    // -----------------------------------------------------------------------

    @GetMapping("/web/doctor/appointments")
    public String doctorAppointments(Model model) {
        addDentistProfile(model);
        model.addAttribute("appointments",      doctorDashboardService.getAppointments());
        model.addAttribute("todayAppointments", doctorDashboardService.getTodayAppointmentList());
        model.addAttribute("todayCount",        doctorDashboardService.getTodayAppointments());
        return "DoctorAppointmentjsp";
    }

    @GetMapping("/doctorAppointment")
    public String doctorAppointmentLegacy(Model model) {
        return doctorAppointments(model);
    }

    // -----------------------------------------------------------------------
    // PATIENTS
    // -----------------------------------------------------------------------

    @GetMapping("/web/doctor/patients")
    public String doctorPatients(Model model) {
        addDentistProfile(model);
        model.addAttribute("patients", doctorDashboardService.getPatients());
        return "DoctorPatients";
    }

    @GetMapping("/doctorPatient")
    public String doctorPatientLegacy(Model model) {
        return doctorPatients(model);
    }

    // -----------------------------------------------------------------------
    // MESSAGES & PROFILE
    // -----------------------------------------------------------------------

    @GetMapping("/web/doctor/messages")
    public String doctorMessages() { return "DoctorMessage"; }

    @GetMapping("/doctorMessage")
    public String doctorMessageLegacy() { return "DoctorMessage"; }

    @GetMapping("/web/doctor/profile")
    public String doctorProfile() { return "DoctorProfile"; }

    @GetMapping("/doctorProfile")
    public String doctorProfileLegacy() { return "DoctorProfile"; }

}
