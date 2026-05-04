package com.mobileApplication.models.web;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "dental_supplies")
public class RecepDentalSupply {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supplyName;
    private String brand;
    private String category;
    private String unit;

    private Integer quantity;
    private Integer lowStockThreshold;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getStatus() {
        if (quantity == null || quantity == 0) return "Out of Stock";
        
        int threshold = lowStockThreshold != null ? lowStockThreshold : 0;
        if (quantity <= threshold) return "Low Stock";
        return "In Stock";
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
