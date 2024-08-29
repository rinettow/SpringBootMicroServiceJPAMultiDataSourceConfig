package com.shop.organic.dto;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.BuildersEstimate;
import com.shop.organic.entity.car.CustomerRequirement;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;

public class AmenitiesAndSpecificationsDTO {
	private int amenitiesAndSpecificationsId;
	private String amenitiesAndSpecificationsName;
	private Set<ProjectsAvailableAmenitiesDTO> projectsAvailableAmenities;
	private Set<BuildersAvailableAmenitiesDTO> buildersAvailableAmenities;
	private List<CustomerRequirementDTO> customerRequirement;
	

	public Set<ProjectsAvailableAmenitiesDTO> getProjectsAvailableAmenities() {
		return projectsAvailableAmenities;
	}

	public void setProjectsAvailableAmenities(Set<ProjectsAvailableAmenitiesDTO> projectsAvailableAmenities) {
		this.projectsAvailableAmenities = projectsAvailableAmenities;
	}

	public Set<BuildersAvailableAmenitiesDTO> getBuildersAvailableAmenities() {
		return buildersAvailableAmenities;
	}

	public void setBuildersAvailableAmenities(Set<BuildersAvailableAmenitiesDTO> buildersAvailableAmenities) {
		this.buildersAvailableAmenities = buildersAvailableAmenities;
	}

	public List<CustomerRequirementDTO> getCustomerRequirement() {
		return customerRequirement;
	}

	public void setCustomerRequirement(List<CustomerRequirementDTO> customerRequirement) {
		this.customerRequirement = customerRequirement;
	}

	public int getAmenitiesAndSpecificationsId() {
		return amenitiesAndSpecificationsId;
	}

	public void setAmenitiesAndSpecificationsId(int amenitiesAndSpecificationsId) {
		this.amenitiesAndSpecificationsId = amenitiesAndSpecificationsId;
	}

	public String getAmenitiesAndSpecificationsName() {
		return amenitiesAndSpecificationsName;
	}

	public void setAmenitiesAndSpecificationsName(String amenitiesAndSpecificationsName) {
		this.amenitiesAndSpecificationsName = amenitiesAndSpecificationsName;
	}

}
