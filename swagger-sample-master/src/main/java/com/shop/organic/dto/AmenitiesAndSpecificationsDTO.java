package com.shop.organic.dto;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;

public class AmenitiesAndSpecificationsDTO {
    private String amenitiesAndSpecificationsId;
    private String amenitiesAndSpecificationsName;
    private List<ProjectsDTO> projects;
	private Set<BuildersAvailableAmenitiesDTO> buildersAvailableAmenities;
	
    
	public List<ProjectsDTO> getProjects() {
		return projects;
	}
	public void setProjects(List<ProjectsDTO> projects) {
		this.projects = projects;
	}
	public String getAmenitiesAndSpecificationsId() {
		return amenitiesAndSpecificationsId;
	}
	public void setAmenitiesAndSpecificationsId(String amenitiesAndSpecificationsId) {
		this.amenitiesAndSpecificationsId = amenitiesAndSpecificationsId;
	}
	public String getAmenitiesAndSpecificationsName() {
		return amenitiesAndSpecificationsName;
	}
	public void setAmenitiesAndSpecificationsName(String amenitiesAndSpecificationsName) {
		this.amenitiesAndSpecificationsName = amenitiesAndSpecificationsName;
	}
	public Set<BuildersAvailableAmenitiesDTO> getBuildersAvailableAmenities() {
		return buildersAvailableAmenities;
	}
	public void setBuildersAvailableAmenities(Set<BuildersAvailableAmenitiesDTO> buildersAvailableAmenities) {
		this.buildersAvailableAmenities = buildersAvailableAmenities;
	}
	
}
