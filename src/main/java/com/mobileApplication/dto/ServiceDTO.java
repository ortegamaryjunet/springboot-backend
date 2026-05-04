package com.mobileApplication.dto;

public class ServiceDTO {

    private Long id;
    private String name;
    private String description;
    private String category;
    private int estimatedDurationMin;

    public ServiceDTO() {}

    public ServiceDTO(Long id, String name, String description, String category, int estimatedDurationMin) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.estimatedDurationMin = estimatedDurationMin;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getEstimatedDurationMin() { return estimatedDurationMin; }
    public void setEstimatedDurationMin(int estimatedDurationMin) { this.estimatedDurationMin = estimatedDurationMin; }
}
