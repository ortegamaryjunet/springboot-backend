package com.mobileApplication.services.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mobileApplication.dto.web.AppointmentDTO;
import com.mobileApplication.dto.web.PatientDTO;
import com.mobileApplication.models.Appointments;
import com.mobileApplication.models.Dentist;
import com.mobileApplication.models.Services;
import com.mobileApplication.models.UserInfo;
import com.mobileApplication.models.web.AdminEmployeeInfo;
import com.mobileApplication.repositories.AppointmentRepository;

import com.mobileApplication.repositories.ServicesRepository;
import com.mobileApplication.repositories.web.AdminEmployeeInfoRepository;
import com.mobileApplication.repositories.web.DentistRepo;
import com.mobileApplication.repositories.web.DentistUserInfoRepository;


import jakarta.servlet.http.HttpSession;

@Service
public class DoctorDashboardService {
	
	private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final DateTimeFormatter DATE_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AppointmentRepository        	appointmentRepository;
    private final DentistUserInfoRepository    	userInfoRepository;   // web-side, has findAllByIdIn()
    private final DentistRepo            		dentistRepository;    // web-side, has findByEmail()
    private final AdminEmployeeInfoRepository  	employeeInfoRepository;
    private final ServicesRepository           	servicesRepository;
    private final HttpSession                  	session;

    public DoctorDashboardService(
            AppointmentRepository       appointmentRepository,
            DentistUserInfoRepository   userInfoRepository,
            DentistRepo           dentistRepository,
            AdminEmployeeInfoRepository employeeInfoRepository,
            ServicesRepository          servicesRepository,
            HttpSession                 session) {
        this.appointmentRepository  = appointmentRepository;
        this.userInfoRepository     = userInfoRepository;
        this.dentistRepository      = dentistRepository;
        this.employeeInfoRepository = employeeInfoRepository;
        this.servicesRepository     = servicesRepository;
        this.session                = session;
    }

    // -----------------------------------------------------------------------
    // RESOLVE LOGGED-IN DENTIST
    // -----------------------------------------------------------------------

    /**
     * Chain:
     *   session("WEB_USER_ID") → AdminEmployeeInfoRepository.findByUserId()
     *   → AdminEmployeeInfo.email → DentistRepository.findByEmail()
     *   → Dentist.id → appointment queries scoped to this dentist
     *
     * Session key "WEB_USER_ID" matches WebAuthController line:
     *   session.setAttribute("WEB_USER_ID", user.getId());
     */
    public Dentist getLoggedInDentist() {
        Long userId = (Long) session.getAttribute("WEB_USER_ID");
        if (userId == null) return null;

        Optional<AdminEmployeeInfo> empOpt = employeeInfoRepository.findByUserId(userId);
        if (empOpt.isEmpty()) return null;

        String email = empOpt.get().getEmail();
        if (email == null || email.isBlank()) return null;

        return dentistRepository.findByEmail(email).orElse(null);
    }

    // -----------------------------------------------------------------------
    // DASHBOARD STATS
    // -----------------------------------------------------------------------

    public long getTotalPatients() {
        Dentist d = getLoggedInDentist();
        if (d == null) return 0;
        return appointmentRepository.countDistinctPatientsByDentistId(d.getId());
    }

    public long getTotalAppointments() {
        Dentist d = getLoggedInDentist();
        if (d == null) return 0;
        return appointmentRepository.countByDentistId(d.getId());
    }

    public long getNewPatients() {
        Dentist d = getLoggedInDentist();
        if (d == null) return 0;
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        return appointmentRepository.countNewPatientsByDentistId(d.getId(), monthStart);
    }

    public long getReturningPatients() {
        Dentist d = getLoggedInDentist();
        if (d == null) return 0;
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        return appointmentRepository.countReturningPatientsByDentistId(d.getId(), monthStart);
    }

    public long getTodayAppointments() {
        Dentist d = getLoggedInDentist();
        if (d == null) return 0;
        LocalDateTime dayStart = LocalDate.now().atStartOfDay();
        LocalDateTime dayEnd   = dayStart.plusDays(1);
        return appointmentRepository.countTodayAppointmentsByDentistId(d.getId(), dayStart, dayEnd);
    }

    // -----------------------------------------------------------------------
    // APPOINTMENTS LIST
    // -----------------------------------------------------------------------

    public List<AppointmentDTO> getAppointments() {
        Dentist d = getLoggedInDentist();
        if (d == null) return Collections.emptyList();
        return mapToAppointmentDTOs(appointmentRepository.findByDentistId(d.getId()));
    }

    public List<AppointmentDTO> getTodayAppointmentList() {
        Dentist d = getLoggedInDentist();
        if (d == null) return Collections.emptyList();
        LocalDateTime dayStart = LocalDate.now().atStartOfDay();
        LocalDateTime dayEnd   = dayStart.plusDays(1);
        return mapToAppointmentDTOs(
                appointmentRepository.findTodayAppointmentsByDentistId(d.getId(), dayStart, dayEnd));
    }

    // -----------------------------------------------------------------------
    // PATIENTS LIST
    // -----------------------------------------------------------------------

    public List<PatientDTO> getPatients() {
        Dentist d = getLoggedInDentist();
        if (d == null) return Collections.emptyList();

        List<Long> patientIds = appointmentRepository.findDistinctPatientIdsByDentistId(d.getId());
        if (patientIds.isEmpty()) return Collections.emptyList();

        List<UserInfo> userInfos = userInfoRepository.findAllByIdIn(patientIds);
        List<Appointments> allAppts = appointmentRepository.findByDentistId(d.getId());

        // Map each patient to their most recent appointment
        Map<Long, Appointments> latestApptMap = new HashMap<>();
        for (Appointments a : allAppts) {
            if (a.getUserInfoId() == null) continue;
            latestApptMap.merge(a.getUserInfoId(), a, (existing, candidate) ->
                    candidate.getScheduledStart().isAfter(existing.getScheduledStart())
                            ? candidate : existing);
        }

        Map<Long, String> serviceNameCache = new HashMap<>();

        return userInfos.stream().map(ui -> {
            PatientDTO dto = new PatientDTO();
            dto.setId(ui.getId());
            dto.setFirstName(ui.getFirstName());
            dto.setLastName(ui.getLastName());
            dto.setMiddleName(ui.getMiddleName());
            dto.setPhoneNumber(ui.getPhoneNumber());
            dto.setDateOfBirth(ui.getDateOfBirth());
            dto.setGender(ui.getGender());

            Appointments latest = latestApptMap.get(ui.getId());
            if (latest != null && latest.getScheduledStart() != null) {
                dto.setLastVisit(latest.getScheduledStart().format(DATE_ISO));
                if (latest.getServiceId() != null) {
                    dto.setLastTreatment(serviceNameCache.computeIfAbsent(
                            latest.getServiceId(),
                            sid -> servicesRepository.findById(sid)
                                    .map(Services::getName)
                                    .orElse("N/A")));
                } else {
                    dto.setLastTreatment("N/A");
                }
            } else {
                dto.setLastVisit("—");
                dto.setLastTreatment("N/A");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------
    // PRIVATE HELPER
    // -----------------------------------------------------------------------

    private List<AppointmentDTO> mapToAppointmentDTOs(List<Appointments> appts) {
        if (appts.isEmpty()) return Collections.emptyList();

        List<Long> userInfoIds = appts.stream()
                .map(Appointments::getUserInfoId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, UserInfo> patientMap = userInfoIds.isEmpty()
                ? Collections.emptyMap()
                : userInfoRepository.findAllByIdIn(userInfoIds)
                        .stream()
                        .collect(Collectors.toMap(UserInfo::getId, ui -> ui));

        Map<Long, String> serviceNameCache = new HashMap<>();

        return appts.stream().map(a -> {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setId(a.getId());
            dto.setStatus(a.getStatus() != null ? a.getStatus() : "pending");
            dto.setBookingType(a.getBookingType());
            dto.setUrgencyLevel(a.getUrgencyLevel());
            dto.setPatientNotes(a.getPatientNotes());
            dto.setDentistId(a.getDentistId());
            dto.setUserInfoId(a.getUserInfoId());

            if (a.getScheduledStart() != null) {
                dto.setScheduledStart(a.getScheduledStart().format(TIME_FMT));
                dto.setScheduledDate(a.getScheduledStart().format(DATE_FMT));
            } else {
                dto.setScheduledStart("—");
                dto.setScheduledDate("—");
            }
            dto.setScheduledEnd(a.getScheduledEnd() != null
                    ? a.getScheduledEnd().format(TIME_FMT) : "—");

            if (a.getUserInfoId() != null) {
                UserInfo ui = patientMap.get(a.getUserInfoId());
                if (ui != null) {
                    String fn = ui.getFirstName() != null ? ui.getFirstName() : "";
                    String ln = ui.getLastName()  != null ? ui.getLastName()  : "";
                    dto.setPatientFullName((fn + " " + ln).trim());
                } else {
                    dto.setPatientFullName("Unknown Patient");
                }
            } else {
                dto.setPatientFullName("Unknown Patient");
            }

            dto.setServiceName(a.getServiceId() != null
                    ? serviceNameCache.computeIfAbsent(a.getServiceId(),
                            sid -> servicesRepository.findById(sid)
                                    .map(Services::getName).orElse("N/A"))
                    : "N/A");

            return dto;
        }).collect(Collectors.toList());
    }

}
