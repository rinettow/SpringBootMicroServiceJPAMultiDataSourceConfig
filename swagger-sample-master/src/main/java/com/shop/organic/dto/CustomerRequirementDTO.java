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
import com.shop.organic.entity.car.Customer;

public class CustomerRequirementDTO {

	
	private int customerRequirementId;
	private int customerId;
	private int amenityAndSpecifiactionId;
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
    private Customer customerForCustomerRequirement;
  	private AmenitiesAndSpecifications amenitiesAndSpecificationsForCustomerRequirement;
	
	
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
	public Customer getCustomerForCustomerRequirement() {
		return customerForCustomerRequirement;
	}
	public void setCustomerForCustomerRequirement(Customer customerForCustomerRequirement) {
		this.customerForCustomerRequirement = customerForCustomerRequirement;
	}
	public AmenitiesAndSpecifications getAmenitiesAndSpecificationsForCustomerRequirement() {
		return amenitiesAndSpecificationsForCustomerRequirement;
	}
	public void setAmenitiesAndSpecificationsForCustomerRequirement(
			AmenitiesAndSpecifications amenitiesAndSpecificationsForCustomerRequirement) {
		this.amenitiesAndSpecificationsForCustomerRequirement = amenitiesAndSpecificationsForCustomerRequirement;
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
