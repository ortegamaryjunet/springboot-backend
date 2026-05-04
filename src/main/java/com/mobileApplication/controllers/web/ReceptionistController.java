package com.mobileApplication.controllers.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobileApplication.dto.web.AppointmentRowDTO;
import com.mobileApplication.dto.web.RecepWalkInRequest;
import com.mobileApplication.models.web.RecepDentalEquipment;
import com.mobileApplication.models.web.RecepDentalMedicine;
import com.mobileApplication.models.web.RecepDentalSupply;
import com.mobileApplication.repositories.DentistRepository;
import com.mobileApplication.repositories.ServicesRepository;
import com.mobileApplication.repositories.web.RecepEquipment;
import com.mobileApplication.repositories.web.RecepMedicine;
import com.mobileApplication.repositories.web.RecepSupply;
import com.mobileApplication.services.web.RecepAppointmentService;
import com.mobileApplication.services.web.RecepDashboardService;
import com.mobileApplication.services.web.RecepInventoryService;
import com.mobileApplication.services.web.RecepMessageService;
import com.mobileApplication.services.web.RecepPatientAccountService;
import com.mobileApplication.services.web.RecepPatientInfoService;


@Controller
public class ReceptionistController {
	
    // ── existing dependencies ────────────────────────────────────────────
    private final RecepDashboardService      dashboardService;
    private final RecepPatientAccountService patientAccountService;
    private final RecepPatientInfoService    patientInfoService;
    private final RecepMedicine              medicineRepository;
    private final RecepSupply                supplyRepository;
    private final RecepEquipment             equipmentRepository;
    private final RecepInventoryService      inventoryService;

    // ── new appointment dependencies ────────────────────────────────────
    private final RecepAppointmentService appointmentService;
    private final DentistRepository       dentistRepository;
    private final ServicesRepository      servicesRepository;
    private final ObjectMapper            objectMapper;
    private final RecepMessageService messageService;

    public ReceptionistController(
            RecepDashboardService      dashboardService,
            RecepPatientAccountService patientAccountService,
            RecepPatientInfoService    patientInfoService,
            RecepMedicine              medicineRepository,
            RecepSupply                supplyRepository,
            RecepEquipment             equipmentRepository,
            RecepInventoryService      inventoryService,
            RecepAppointmentService    appointmentService,
            DentistRepository          dentistRepository,
            ServicesRepository         servicesRepository,
            ObjectMapper               objectMapper,
            RecepMessageService 		messageService) {

        this.dashboardService      = dashboardService;
        this.patientAccountService = patientAccountService;
        this.patientInfoService    = patientInfoService;
        this.medicineRepository    = medicineRepository;
        this.supplyRepository      = supplyRepository;
        this.equipmentRepository   = equipmentRepository;
        this.inventoryService      = inventoryService;
        this.appointmentService    = appointmentService;
        this.dentistRepository     = dentistRepository;
        this.servicesRepository    = servicesRepository;
        this.objectMapper          = objectMapper;
        this.messageService = messageService;
    }

    // ════════════════════════════════════════════════════════════════════
    //  APPOINTMENT PAGE
    // ════════════════════════════════════════════════════════════════════

    /**
     * Renders the appointment page.
     * Injects real data from the shared appointments table so mobile-booked
     * appointments appear automatically in the receptionist view.
     */
    @GetMapping("/web/recepAppointment")
    public String appointmentPage(Model model) {
        List<AppointmentRowDTO> appointments = appointmentService.getTodaysAppointments();

        // Inject as JSON string so JS can bootstrap instantly (no AJAX flash)
        String appointmentsJson = "[]";
        try {
            appointmentsJson = objectMapper.writeValueAsString(appointments);
        } catch (JsonProcessingException ignored) {}

        model.addAttribute("appointments",     appointments);
        model.addAttribute("appointmentsJson", appointmentsJson);
        model.addAttribute("dentists",         dentistRepository.findAll());
        model.addAttribute("services",         servicesRepository.findAll());
        model.addAttribute("today",            LocalDate.now().toString());

        return "ReceptionAppointment";
    }

    // ── AJAX: refresh list (called by JS polling every 30s) ─────────────

    /**
     * Returns today's appointments as JSON.
     * Supports optional dentistId / serviceId filter params.
     */
    @GetMapping("/web/api/appointments/today")
    @ResponseBody
    public ResponseEntity<List<AppointmentRowDTO>> todayJson(
            @RequestParam(required = false) Long dentistId,
            @RequestParam(required = false) Long serviceId) {
        return ResponseEntity.ok(appointmentService.getTodaysAppointments(dentistId, serviceId));
    }

    // ── AJAX: mark arrived → moves to queue ─────────────────────────────

    @PostMapping("/web/api/appointments/{id}/arrive")
    @ResponseBody
    public ResponseEntity<?> markArrived(@PathVariable Long id) {
        try {
            appointmentService.markArrived(id);
            return ResponseEntity.ok(Map.of("status", "ok", "message", "Patient marked as arrived."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── AJAX: mark done → removes from queue ────────────────────────────

    @PostMapping("/web/api/appointments/{id}/done")
    @ResponseBody
    public ResponseEntity<?> markDone(@PathVariable Long id) {
        try {
            appointmentService.markDone(id);
            return ResponseEntity.ok(Map.of("status", "ok", "message", "Appointment completed."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── AJAX: cancel ─────────────────────────────────────────────────────

    @PostMapping("/web/api/appointments/{id}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelWeb(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok(Map.of("status", "ok", "message", "Appointment cancelled."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── AJAX: add walk-in ────────────────────────────────────────────────

    @PostMapping("/web/api/appointments/walkin")
    @ResponseBody
    public ResponseEntity<?> addWalkIn(@RequestBody RecepWalkInRequest req) {
        try {
            AppointmentRowDTO saved = appointmentService.addWalkIn(req);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  EXISTING ROUTES (unchanged)
    // ════════════════════════════════════════════════════════════════════

    @GetMapping("/web/recepDashboard")
    public String receptionistDashboard(Model model) {
        model.addAttribute("totalPatients",           dashboardService.getTotalPatients());
        model.addAttribute("totalAppointments",       dashboardService.getTotalAppointments());
        model.addAttribute("pendingAppointments",     dashboardService.getPendingAppointments());
        model.addAttribute("rescheduledAppointments", dashboardService.getRescheduledAppointments());
        model.addAttribute("cancelledAppointments",   dashboardService.getCancelledAppointments());
        model.addAttribute("activeDentists",          dashboardService.getActiveDentists());
        return "ReceptionDashboard";
    }
    
    @GetMapping("/web/recep/dashboard")
    public String recepDashboardAlias(Model model) {
        return receptionistDashboard(model);
    }

    @GetMapping("/web/recepPaccounts")
    public String showPatientAccounts(Model model) {
        model.addAttribute("patientAccounts",      patientAccountService.getAllPatientAccounts());
        model.addAttribute("totalPatientAccounts", patientAccountService.getTotalPatientAccounts());
        model.addAttribute("activePatients",       patientAccountService.getActivePatients());
        model.addAttribute("inactivePatients",     patientAccountService.getInactivePatients());
        return "ReceptionPAccount";
    }

    @PostMapping("/web/recePaccount/update")
    public String updatePatientAccount(
            @RequestParam Long   infoId,
            @RequestParam Long   userId,
            @RequestParam String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String status) {
        patientAccountService.updatePatientAccount(infoId, userId, firstName, middleName, lastName, username, status);
        return "redirect:/web/recepPaccounts";
    }

    @PostMapping("/web/recepPaccounts/create")
    public String createPatientAccount(
            @RequestParam String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        try {
            patientAccountService.createPatientAccount(firstName, middleName, lastName, email, username, password);
            return "redirect:/web/recepPaccounts";
        } catch (RuntimeException e) {
            model.addAttribute("patientAccounts",      patientAccountService.getAllPatientAccounts());
            model.addAttribute("totalPatientAccounts", patientAccountService.getTotalPatientAccounts());
            model.addAttribute("activePatients",       patientAccountService.getActivePatients());
            model.addAttribute("inactivePatients",     patientAccountService.getInactivePatients());
            model.addAttribute("createErrorMessage",   e.getMessage());
            model.addAttribute("openCreateModal",      true);
            model.addAttribute("oldFirstName",         firstName);
            model.addAttribute("oldMiddleName",        middleName);
            model.addAttribute("oldLastName",          lastName);
            model.addAttribute("oldEmail",             email);
            model.addAttribute("oldUsername",          username);
            return "ReceptionPAccount";
        }
    }

    @GetMapping("/web/recepPatients")
    public String showPatients(Model model) {
        model.addAttribute("patients", patientInfoService.getAllPatients());
        return "ReceptionPatients";
    }

    @PostMapping("/web/recepPatients/update")
    public String updatePatientInfo(
            @RequestParam Long   infoId,
            @RequestParam String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam String lastName,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String address) {
        patientInfoService.updatePatientInfo(infoId, firstName, middleName, lastName,
                dateOfBirth, gender, email, phoneNumber, address);
        return "redirect:/web/recepPatients";
    }

    @GetMapping("/web/recepInventory")
    public String inventoryPage(Model model) {
        model.addAttribute("medicines",      medicineRepository.findAll());
        model.addAttribute("supplies",       supplyRepository.findAll());
        model.addAttribute("equipmentList",  equipmentRepository.findAll());
        model.addAttribute("lowMedicineCount",   inventoryService.getLowStockMedicineCount());
        model.addAttribute("lowSupplyCount",     inventoryService.getLowStockSupplyCount());
        model.addAttribute("lowEquipmentCount",  inventoryService.getLowStockEquipmentCount());
        return "ReceptionInventory";
    }

    @PostMapping("/web/inventory/medicine/update")
    public String updateMedicine(
            @RequestParam Long    id,
            @RequestParam String  medicineName,
            @RequestParam(required = false) String genericName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String form,
            @RequestParam(required = false) String dosage,
            @RequestParam(required = false) String unit,
            @RequestParam Integer quantity,
            @RequestParam Integer lowStockThreshold) {
        RecepDentalMedicine med = medicineRepository.findById(id).orElseThrow();
        med.setMedicineName(medicineName); med.setGenericName(genericName);
        med.setCategory(category);        med.setForm(form);
        med.setDosage(dosage);            med.setUnit(unit);
        med.setQuantity(quantity);        med.setLowStockThreshold(lowStockThreshold);
        medicineRepository.save(med);
        return "redirect:/web/recepInventory";
    }

    @PostMapping("/web/inventory/supply/update")
    public String updateSupply(
            @RequestParam Long    id,
            @RequestParam String  supplyName,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String unit,
            @RequestParam Integer quantity,
            @RequestParam Integer lowStockThreshold) {
        RecepDentalSupply supply = supplyRepository.findById(id).orElseThrow();
        supply.setSupplyName(supplyName); supply.setBrand(brand);
        supply.setCategory(category);     supply.setUnit(unit);
        supply.setQuantity(quantity);     supply.setLowStockThreshold(lowStockThreshold);
        supplyRepository.save(supply);
        return "redirect:/web/recepInventory";
    }

    @PostMapping("/web/inventory/equipment/update")
    public String updateEquipment(
            @RequestParam Long   id,
            @RequestParam String equipmentName,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String modelNumber,
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) String location,
            @RequestParam Integer  quantity,
            @RequestParam Integer  lowStockThreshold,
            @RequestParam(required = false) String    maintenanceStatus,
            @RequestParam(required = false) String    assignedTo,
            @RequestParam(required = false) LocalDate lastMaintenance,
            @RequestParam(required = false) LocalDate nextMaintenance) {
        RecepDentalEquipment eq = equipmentRepository.findById(id).orElseThrow();
        eq.setEquipmentName(equipmentName); eq.setBrand(brand);
        eq.setCategory(category);           eq.setModelNumber(modelNumber);
        eq.setSerialNumber(serialNumber);   eq.setLocation(location);
        eq.setQuantity(quantity);           eq.setLowStockThreshold(lowStockThreshold);
        eq.setMaintenanceStatus(maintenanceStatus); eq.setAssignedTo(assignedTo);
        eq.setLastMaintenance(lastMaintenance);     eq.setNextMaintenance(nextMaintenance);
        equipmentRepository.save(eq);
        return "redirect:/web/recepInventory";
    }

    @PostMapping("/web/inventory/equipment/maintenance/update")
    public String updateEquipmentMaintenance(
            @RequestParam Long id,
            @RequestParam(required = false) String    maintenanceStatus,
            @RequestParam(required = false) String    assignedTo,
            @RequestParam(required = false) LocalDate lastMaintenance,
            @RequestParam(required = false) LocalDate nextMaintenance) {
        RecepDentalEquipment eq = equipmentRepository.findById(id).orElseThrow();
        eq.setMaintenanceStatus(maintenanceStatus); eq.setAssignedTo(assignedTo);
        eq.setLastMaintenance(lastMaintenance);     eq.setNextMaintenance(nextMaintenance);
        equipmentRepository.save(eq);
        return "redirect:/web/recepInventory";
    }
    
    @PostMapping("/web/api/appointments/{id}/link-account")
    @ResponseBody
    public ResponseEntity<?> linkAccount(
            @PathVariable Long id,
            @RequestBody Map<String, Object> req) {
        try {
            Long userInfoId = Long.valueOf(req.get("userInfoId").toString());
            appointmentService.linkPatientAccount(id, userInfoId);
            return ResponseEntity.ok(Map.of("status", "ok"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/web/receptionMessages")
    public String messagePage(@RequestParam(required = false) Long patientId, Model model) {
        model.addAttribute("selectedPatientId", patientId);
        return "ReceptionMessage";
    }
    
    @GetMapping("/web/api/messages/{patientId}")
    @ResponseBody
    public ResponseEntity<?> getMessages(@PathVariable Long patientId) {
        try {
            return ResponseEntity.ok(messageService.getConversation(patientId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/web/api/messages/send")
    @ResponseBody
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> req) {
        try {
            Long patientId = Long.valueOf(req.get("patientId").toString());
            String message = req.get("message").toString();

            messageService.sendFromReceptionist(patientId, message);

            return ResponseEntity.ok(Map.of("status", "sent"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/web/api/messages/{patientId}/read")
    @ResponseBody
    public ResponseEntity<?> markAsRead(@PathVariable Long patientId) {
        messageService.markAsRead(patientId);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

}
