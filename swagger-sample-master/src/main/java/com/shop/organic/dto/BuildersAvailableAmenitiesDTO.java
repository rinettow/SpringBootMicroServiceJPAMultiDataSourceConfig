package com.shop.organic.dto;

import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;

public class BuildersAvailableAmenitiesDTO {
    
	private int builderId;
	private int amenitiesAndSpecificationsId;
	private BuilderDTO builder;
	private AmenitiesAndSpecificationsDTO amenitiesAndSpecifications;
	
	
	public int getBuilderId() {
		return builderId;
	}
	public void setBuilderId(int builderId) {
		this.builderId = builderId;
	}
	public int getAmenitiesAndSpecificationsId() {
		return amenitiesAndSpecificationsId;
	}
	public void setAmenitiesAndSpecificationsId(int amenitiesAndSpecificationsId) {
		this.amenitiesAndSpecificationsId = amenitiesAndSpecificationsId;
	}
	public BuilderDTO getBuilder() {
		return builder;
	}
	public void setBuilder(BuilderDTO builder) {
		this.builder = builder;
	}
	public AmenitiesAndSpecificationsDTO getAmenitiesAndSpecifications() {
		return amenitiesAndSpecifications;
	}
	public void setAmenitiesAndSpecifications(AmenitiesAndSpecificationsDTO amenitiesAndSpecifications) {
		this.amenitiesAndSpecifications = amenitiesAndSpecifications;
	}
	
}
