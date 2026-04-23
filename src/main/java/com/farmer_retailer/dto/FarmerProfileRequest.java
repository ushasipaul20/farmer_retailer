package com.farmer_retailer.dto;

public class FarmerProfileRequest {

    private String farmName;
    private String phone;
    private String village;
    private String district;
    private String state;
    private Double farmSize;
    private String cropTypes;
    private Integer experienceYears;

    // ===================== Getters =====================

    public String getFarmName() {
        return farmName;
    }

    public String getPhone() {
        return phone;
    }

    public String getVillage() {
        return village;
    }

    public String getDistrict() {
        return district;
    }

    public String getState() {
        return state;
    }

    public Double getFarmSize() {
        return farmSize;
    }

    public String getCropTypes() {
        return cropTypes;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    // ===================== Setters =====================

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setFarmSize(Double farmSize) {
        this.farmSize = farmSize;
    }

    public void setCropTypes(String cropTypes) {
        this.cropTypes = cropTypes;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }
}
