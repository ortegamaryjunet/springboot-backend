package com.mobileApplication.models.web;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "dental_equipment")
public class RecepDentalEquipment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String equipmentName;
    private String brand;
    private String category;
    private String modelNumber;
    private String serialNumber;

    private LocalDateTime purchaseDate;
    private LocalDate warrantyDate;

    private String location;

    private Integer quantity;
    private Integer lowStockThreshold;
    
    private String maintenanceStatus;
    private String assignedTo;
    private LocalDate lastMaintenance;
    private LocalDate nextMaintenance;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getStatus() {
        if (quantity == null || quantity == 0) return "Unavailable";
        
        int threshold = lowStockThreshold != null ? lowStockThreshold : 0;
        if (quantity <= threshold) return "Low Stock";
        return "Available";
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public LocalDateTime getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDateTime purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public LocalDate getWarrantyDate() {
		return warrantyDate;
	}

	public void setWarrantyDate(LocalDate warrantyDate) {
		this.warrantyDate = warrantyDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getLowStockThreshold() {
		return lowStockThreshold;
	}

	public void setLowStockThreshold(Integer lowStockThreshold) {
		this.lowStockThreshold = lowStockThreshold;
	}

	public String getMaintenanceStatus() {
		return maintenanceStatus;
	}

	public void setMaintenanceStatus(String maintenanceStatus) {
		this.maintenanceStatus = maintenanceStatus;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDate getLastMaintenance() {
		return lastMaintenance;
	}

	public void setLastMaintenance(LocalDate lastMaintenance) {
		this.lastMaintenance = lastMaintenance;
	}

	public LocalDate getNextMaintenance() {
		return nextMaintenance;
	}

	public void setNextMaintenance(LocalDate nextMaintenance) {
		this.nextMaintenance = nextMaintenance;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
