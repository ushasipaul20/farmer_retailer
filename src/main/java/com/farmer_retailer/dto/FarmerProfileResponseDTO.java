package com.farmer_retailer.dto;

import com.farmer_retailer.model.Farmer;

public class FarmerProfileResponseDTO {
    private Long id;
    private String farmName;
    private String phone;
    private String district;
    private boolean profileCompleted;
    private String name; // User's name

    public FarmerProfileResponseDTO(Farmer farmer) {
//        this.id = farmer.getId();
        this.farmName = farmer.getFarmName();
        this.phone = farmer.getPhone();
        this.district = farmer.getDistrict();
        this.profileCompleted = farmer.isProfileCompleted();
        this.name = farmer.getUser() != null ? farmer.getUser().getName() : null;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getFarmName() { return farmName; }
    public String getPhone() { return phone; }
    public String getDistrict() { return district; }
    public boolean isProfileCompleted() { return profileCompleted; }
    public String getName() { return name; }
}
