package com.shop.organic.dto;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;

public class AmenitiesAndSpecificationsDTO {
    private int amenitiesAndSpecificationsId;
    private String amenitiesAndSpecificationsName;
    private Set<ProjectsAvailableAmenities> projectsAvailableAmenities;
    private Set<BuildersAvailableAmenities> buildersAvailableAmenities;
	
    
	
	
	public Set<ProjectsAvailableAmenities> getProjectsAvailableAmenities() {
		return projectsAvailableAmenities;
	}
	public void setProjectsAvailableAmenities(Set<ProjectsAvailableAmenities> projectsAvailableAmenities) {
		this.projectsAvailableAmenities = projectsAvailableAmenities;
	}
	public Set<BuildersAvailableAmenities> getBuildersAvailableAmenities() {
		return buildersAvailableAmenities;
	}
	public void setBuildersAvailableAmenities(Set<BuildersAvailableAmenities> buildersAvailableAmenities) {
		this.buildersAvailableAmenities = buildersAvailableAmenities;
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
