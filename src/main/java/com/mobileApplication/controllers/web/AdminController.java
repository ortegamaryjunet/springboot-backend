package com.mobileApplication.controllers.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mobileApplication.models.UserInfo;
import com.mobileApplication.models.web.AdminEmployeeInfo;
import com.mobileApplication.repositories.web.AdminEmployeeInfoRepository;
import com.mobileApplication.repositories.web.RecepAppointment;
import com.mobileApplication.repositories.web.RecepDentist;
import com.mobileApplication.repositories.web.RecepEquipment;
import com.mobileApplication.repositories.web.RecepMedicine;
import com.mobileApplication.repositories.web.RecepSupply;
import com.mobileApplication.repositories.web.RecepUser;
import com.mobileApplication.repositories.web.RecepUserInfo;

@Controller
public class AdminController {
	
	private final RecepUser recepUser;
    private final RecepUserInfo recepUserInfo;
    private final RecepAppointment recepAppointment;
    private final RecepDentist recepDentist;
    private final RecepMedicine recepMedicine;
    private final RecepSupply recepSupply;
    private final RecepEquipment recepEquipment;
    private final AdminEmployeeInfoRepository employeeInfoRepository;

    public AdminController(
            RecepUser recepUser,
            RecepUserInfo recepUserInfo,
            RecepAppointment recepAppointment,
            RecepDentist recepDentist,
            RecepMedicine recepMedicine,
            RecepSupply recepSupply,
            RecepEquipment recepEquipment,
            AdminEmployeeInfoRepository employeeInfoRepository
    ) {
        this.recepUser = recepUser;
        this.recepUserInfo = recepUserInfo;
        this.recepAppointment = recepAppointment;
        this.recepDentist = recepDentist;
        this.recepMedicine = recepMedicine;
        this.recepSupply = recepSupply;
        this.recepEquipment = recepEquipment;
        this.employeeInfoRepository = employeeInfoRepository;
    }

    @GetMapping("/web/admin/dashboard")
    public String adminDashboard(Model model) {

        long totalProducts =
                recepMedicine.count() +
                recepSupply.count() +
                recepEquipment.count();

        long inStock =
                recepMedicine.findAll().stream()
                        .filter(m -> "In Stock".equalsIgnoreCase(m.getStatus()))
                        .count()
                + recepSupply.findAll().stream()
                        .filter(s -> "In Stock".equalsIgnoreCase(s.getStatus()))
                        .count()
                + recepEquipment.findAll().stream()
                        .filter(e -> "Available".equalsIgnoreCase(e.getStatus()))
                        .count();

        long lowStock =
                recepMedicine.findAll().stream()
                        .filter(m -> "Low Stock".equalsIgnoreCase(m.getStatus()))
                        .count()
                + recepSupply.findAll().stream()
                        .filter(s -> "Low Stock".equalsIgnoreCase(s.getStatus()))
                        .count()
                + recepEquipment.findAll().stream()
                        .filter(e -> "Low Stock".equalsIgnoreCase(e.getStatus()))
                        .count();

        long outStock =
                recepMedicine.findAll().stream()
                        .filter(m -> "Out of Stock".equalsIgnoreCase(m.getStatus()))
                        .count()
                + recepSupply.findAll().stream()
                        .filter(s -> "Out of Stock".equalsIgnoreCase(s.getStatus()))
                        .count()
                + recepEquipment.findAll().stream()
                        .filter(e -> "Unavailable".equalsIgnoreCase(e.getStatus()))
                        .count();

        model.addAttribute("totalUsers", recepUser.count());
        model.addAttribute("activeUsers", recepUser.countByStatusIgnoreCase("ACTIVE"));
        model.addAttribute("totalPatients", recepUserInfo.count());
        model.addAttribute("totalAppointments", recepAppointment.count());
        model.addAttribute("totalDentists", recepDentist.count());

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("inStock", inStock);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("outStock", outStock);
        
        model.addAttribute("medicines", recepMedicine.findAll());
        model.addAttribute("supplies", recepSupply.findAll());
        model.addAttribute("equipmentList", recepEquipment.findAll());

        return "AdminDashboard";
    }

    @GetMapping("/web/admin/patients")
    public String adminPatient(Model model) {

        List<UserInfo> patients = recepUserInfo.findAll()
                .stream()
                .filter(u -> u.getUserModel() != null
                          && "PATIENT".equalsIgnoreCase(u.getUserModel().getRole()))
                .toList();

        model.addAttribute("patients", patients);

        return "AdminPatients";
    }

    @GetMapping("/web/admin/employees")
    public String adminEmployee(Model model) {
        model.addAttribute("employees", employeeInfoRepository.findAll());
        return "AdminEmployee";
    }

    @GetMapping("/web/admin/inventory")
    public String adminInventory(Model model) {
        model.addAttribute("medicines", recepMedicine.findAll());
        model.addAttribute("supplies", recepSupply.findAll());
        model.addAttribute("equipmentList", recepEquipment.findAll());

        return "AdminInventory";
    }

    @GetMapping("/web/admin/report")
    public String adminReport(Model model) {
        model.addAttribute("appointments", recepAppointment.findAll());
        model.addAttribute("dentists", recepDentist.findAll());

        return "AdminReport";
    }

    @GetMapping("/web/admin/audit")
    public String adminAudit() {
        return "AdminAudit";
    }

    @GetMapping("/web/admin/settings")
    public String adminSettings() {
        return "AdminSettings";
    }

    @GetMapping("/web/admin/register")
    public String adminRegister() {
        return "AdminRegister";
    }
}
