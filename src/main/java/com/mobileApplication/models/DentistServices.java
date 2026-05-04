package com.mobileApplication.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "dentist_services_table")
@IdClass(DentistServices.DentistServiceId.class)
public class DentistServices {

    @Id
    @Column(name = "dentist_id")
    private Long dentistId;

    @Id
    @Column(name = "service_id")
    private Long serviceId;

    // Composite PK class
    public static class DentistServiceId implements Serializable {
        private Long dentistId;
        private Long serviceId;

        public DentistServiceId() {}

        public DentistServiceId(Long dentistId, Long serviceId) {
            this.dentistId = dentistId;
            this.serviceId = serviceId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DentistServiceId)) return false;
            DentistServiceId that = (DentistServiceId) o;
            return Objects.equals(dentistId, that.dentistId) &&
                   Objects.equals(serviceId, that.serviceId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dentistId, serviceId);
        }
    }

	public Long getDentistId() {
		return dentistId;
	}

	public void setDentistId(Long dentistId) {
		this.dentistId = dentistId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
    
}
