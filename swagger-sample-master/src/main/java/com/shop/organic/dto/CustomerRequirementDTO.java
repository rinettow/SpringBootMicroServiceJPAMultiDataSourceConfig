package com.shop.organic.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.BuildersEstimate;
import com.shop.organic.entity.car.Customer;
import com.shop.organic.entity.car.SiteLocation;

public class CustomerRequirementDTO {

	private int customerRequirementId;
	private int customerId;
	private int amenityAndSpecifiactionId;
	private String projectBudgetFullHouseConstructionWithMaterial;
	private String requirementStatus;
	private String bhkCount;
	private int totalSquareFeet;
	private int totalWallSquareFeet;
	private String planImagePath;
	private String landImagePath;
	private String brickType;
	private boolean pillerBeamRequired;
	private String floorType;
	private String woodType;
	private int paintCoatCount;
	private int paintWallPuttyCount;
	private String paintBrand;
	private String paintQuality;
	private String plumbingBrand;
	private String electricalBrand;
	private String cementBrand;
	private String steelBrand;
	private String tilesFloorWallBrand;
	private CustomerDTO customerForCustomerRequirement;
	private AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsForCustomerRequirement;
	private List<BuildersEstimateDTO> buildersEstimate;
	private byte[] planPDFFileFormat;
	private byte[] landImagePNGorJPGFileFormat;
    private String state;
	private String district;
	private String doorNumber;
	private String streetFirst;
	private String streetSecond;
	private String landmark;
	private String city;
	private String pincode;
	private String country;
	private List<SiteLocationDTO> siteLocations;
	private String isRequirementViewedByBuilder;
	
	

	public String getProjectBudgetFullHouseConstructionWithMaterial() {
		return projectBudgetFullHouseConstructionWithMaterial;
	}

	public void setProjectBudgetFullHouseConstructionWithMaterial(String projectBudgetFullHouseConstructionWithMaterial) {
		this.projectBudgetFullHouseConstructionWithMaterial = projectBudgetFullHouseConstructionWithMaterial;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public String getStreetFirst() {
		return streetFirst;
	}

	public void setStreetFirst(String streetFirst) {
		this.streetFirst = streetFirst;
	}

	public String getStreetSecond() {
		return streetSecond;
	}

	public void setStreetSecond(String streetSecond) {
		this.streetSecond = streetSecond;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIsRequirementViewedByBuilder() {
		return isRequirementViewedByBuilder;
	}

	public void setIsRequirementViewedByBuilder(String isRequirementViewedByBuilder) {
		this.isRequirementViewedByBuilder = isRequirementViewedByBuilder;
	}

	public List<SiteLocationDTO> getSiteLocations() {
		return siteLocations;
	}

	public void setSiteLocations(List<SiteLocationDTO> siteLocations) {
		this.siteLocations = siteLocations;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public AmenitiesAndSpecificationsDTO getAmenitiesAndSpecificationsForCustomerRequirement() {
		return amenitiesAndSpecificationsForCustomerRequirement;
	}

	public void setAmenitiesAndSpecificationsForCustomerRequirement(
			AmenitiesAndSpecificationsDTO amenitiesAndSpecificationsForCustomerRequirement) {
		this.amenitiesAndSpecificationsForCustomerRequirement = amenitiesAndSpecificationsForCustomerRequirement;
	}

	public List<BuildersEstimateDTO> getBuildersEstimate() {
		return buildersEstimate;
	}

	public void setBuildersEstimate(List<BuildersEstimateDTO> buildersEstimate) {
		this.buildersEstimate = buildersEstimate;
	}

	
	
	public byte[] getPlanPDFFileFormat() {
		return planPDFFileFormat;
	}

	public void setPlanPDFFileFormat(byte[] planPDFFileFormat) {
		this.planPDFFileFormat = planPDFFileFormat;
	}

	public byte[] getLandImagePNGorJPGFileFormat() {
		return landImagePNGorJPGFileFormat;
	}

	public void setLandImagePNGorJPGFileFormat(byte[] landImagePNGorJPGFileFormat) {
		this.landImagePNGorJPGFileFormat = landImagePNGorJPGFileFormat;
	}

	public int getCustomerRequirementId() {
		return customerRequirementId;
	}

	public void setCustomerRequirementId(int customerRequirementId) {
		this.customerRequirementId = customerRequirementId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getAmenityAndSpecifiactionId() {
		return amenityAndSpecifiactionId;
	}

	public void setAmenityAndSpecifiactionId(int amenityAndSpecifiactionId) {
		this.amenityAndSpecifiactionId = amenityAndSpecifiactionId;
	}

	public String getRequirementStatus() {
		return requirementStatus;
	}

	public void setRequirementStatus(String requirementStatus) {
		this.requirementStatus = requirementStatus;
	}

	public String getBhkCount() {
		return bhkCount;
	}

	public void setBhkCount(String bhkCount) {
		this.bhkCount = bhkCount;
	}


	public CustomerDTO getCustomerForCustomerRequirement() {
		return customerForCustomerRequirement;
	}

	public void setCustomerForCustomerRequirement(CustomerDTO customerForCustomerRequirement) {
		this.customerForCustomerRequirement = customerForCustomerRequirement;
	}

	

	public int getTotalSquareFeet() {
		return totalSquareFeet;
	}

	public void setTotalSquareFeet(int totalSquareFeet) {
		this.totalSquareFeet = totalSquareFeet;
	}

	public int getTotalWallSquareFeet() {
		return totalWallSquareFeet;
	}

	public void setTotalWallSquareFeet(int totalWallSquareFeet) {
		this.totalWallSquareFeet = totalWallSquareFeet;
	}

	public String getPlanImagePath() {
		return planImagePath;
	}

	public void setPlanImagePath(String planImagePath) {
		this.planImagePath = planImagePath;
	}

	public String getLandImagePath() {
		return landImagePath;
	}

	public void setLandImagePath(String landImagePath) {
		this.landImagePath = landImagePath;
	}

	public String getBrickType() {
		return brickType;
	}

	public void setBrickType(String brickType) {
		this.brickType = brickType;
	}

	public boolean isPillerBeamRequired() {
		return pillerBeamRequired;
	}

	public void setPillerBeamRequired(boolean pillerBeamRequired) {
		this.pillerBeamRequired = pillerBeamRequired;
	}

	public String getFloorType() {
		return floorType;
	}

	public void setFloorType(String floorType) {
		this.floorType = floorType;
	}

	public String getWoodType() {
		return woodType;
	}

	public void setWoodType(String woodType) {
		this.woodType = woodType;
	}

	public int getPaintCoatCount() {
		return paintCoatCount;
	}

	public void setPaintCoatCount(int paintCoatCount) {
		this.paintCoatCount = paintCoatCount;
	}

	public int getPaintWallPuttyCount() {
		return paintWallPuttyCount;
	}

	public void setPaintWallPuttyCount(int paintWallPuttyCount) {
		this.paintWallPuttyCount = paintWallPuttyCount;
	}

	public String getPaintBrand() {
		return paintBrand;
	}

	public void setPaintBrand(String paintBrand) {
		this.paintBrand = paintBrand;
	}

	public String getPaintQuality() {
		return paintQuality;
	}

	public void setPaintQuality(String paintQuality) {
		this.paintQuality = paintQuality;
	}

	public String getPlumbingBrand() {
		return plumbingBrand;
	}

	public void setPlumbingBrand(String plumbingBrand) {
		this.plumbingBrand = plumbingBrand;
	}

	public String getElectricalBrand() {
		return electricalBrand;
	}

	public void setElectricalBrand(String electricalBrand) {
		this.electricalBrand = electricalBrand;
	}

	public String getCementBrand() {
		return cementBrand;
	}

	public void setCementBrand(String cementBrand) {
		this.cementBrand = cementBrand;
	}

	public String getSteelBrand() {
		return steelBrand;
	}

	public void setSteelBrand(String steelBrand) {
		this.steelBrand = steelBrand;
	}

	public String getTilesFloorWallBrand() {
		return tilesFloorWallBrand;
	}

	public void setTilesFloorWallBrand(String tilesFloorWallBrand) {
		this.tilesFloorWallBrand = tilesFloorWallBrand;
	}

}
