package com.shop.organic.entity.car;

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

@Entity
@Table(name = "CUSTOMER_REQUIREMENT")
public class CustomerRequirement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMER_REQUIREMENT_ID")
	private int customerRequirementId;

	@Column(name = "CUSTOMER_ID")
	private int customerId;

	@Column(name = "AMENITIES_AND_SPECIFICATIONS_ID")
	private int amenityAndSpecifiactionId;

	@Column(name = "REQUIREMENT_STATUS")
	private String requirementStatus;

	@Column(name = "BHK_COUNT")
	private String bhkCount;

	@Column(name = "TOTAL_SQUARE_FEET")
	private int totalSquareFeet;

	@Column(name = "TOTAL_WALL_SQUARE_FEET")
	private int totalWallSquareFeet;

	@Column(name = "PLAN_IMAGE_PATH")
	private String planImagePath;

	@Column(name = "lAND_IMAGE_PATH")
	private String landImagePath;

	@Column(name = "BRICK_TYPE")
	private String brickType;

	@Column(name = "PILLLER_BEAM_REQUIRED")
	private boolean pillerBeamRequired;

	@Column(name = "FLOOR_TYPE")
	private String floorType;

	@Column(name = "WOOD_TYPE")
	private String woodType;

	@Column(name = "PAINT_COAT_COUNT")
	private int paintCoatCount;

	@Column(name = "PAINT_WALL_PUTTY_COUNT")
	private int paintWallPuttyCount;

	@Column(name = "PAINT_BRAND")
	private String paintBrand;

	@Column(name = "PAINT_QUALITY")
	private String paintQuality;

	@Column(name = "PLUMBING_BRAND")
	private String plumbingBrand;

	@Column(name = "ELECTRICAL_BRAND")
	private String electricalBrand;

	@Column(name = "CEMENT_BRAND")
	private String cementBrand;

	@Column(name = "STEEL_BRAND")
	private String steelBrand;

	@Column(name = "TILES_FLOOR_WALL_BRAND")
	private String tilesFloorWallBrand;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID", insertable = false, updatable = false)
	private Customer customerForCustomerRequirement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AMENITIES_AND_SPECIFICATIONS_ID", insertable = false, updatable = false)
	private AmenitiesAndSpecifications amenitiesAndSpecificationsForCustomerRequirement;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customerRequirementForBuildersEstimate")
	private List<BuildersEstimate> buildersEstimate;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "DISTRICT")
	private String district;
	
	
	
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

	public List<BuildersEstimate> getBuildersEstimate() {
		return buildersEstimate;
	}

	public void setBuildersEstimate(List<BuildersEstimate> buildersEstimate) {
		this.buildersEstimate = buildersEstimate;
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
