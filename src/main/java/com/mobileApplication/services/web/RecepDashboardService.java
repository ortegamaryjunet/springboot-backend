package com.mobileApplication.services.web;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mobileApplication.repositories.web.RecepAppointment;
import com.mobileApplication.repositories.web.RecepDentist;
import com.mobileApplication.repositories.web.RecepUserInfo;

@Service
public class RecepDashboardService {
	
	private final RecepAppointment recepAppointment;
    private final RecepDentist recepDentist;
    private final RecepUserInfo recepUserInfo;

    public RecepDashboardService(
    		RecepAppointment recepAppointment,
    		RecepDentist recepDentist,
    		RecepUserInfo recepUserInfo
    ) {
        this.recepAppointment = recepAppointment;
        this.recepDentist = recepDentist;
        this.recepUserInfo = recepUserInfo;
    }

    public long getTotalPatients() {
        return recepUserInfo.count();
    }

    public long getTotalAppointments() {
        return recepAppointment.count();
    }

    public long getPendingAppointments() {
        return recepAppointment.countByStatusAndScheduledStartGreaterThanEqual("pending", LocalDateTime.now());
    }

    public long getRescheduledAppointments() {
        return recepAppointment.countByStatus("rescheduled");
    }

    public long getCancelledAppointments() {
        return recepAppointment.countByStatus("cancelled");
    }

    public long getActiveDentists() {
        return recepDentist.countByIsActive(true);
    }

}
