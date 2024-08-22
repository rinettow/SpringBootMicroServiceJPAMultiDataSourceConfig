package com.shop.organic.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.shop.organic.entity.car.Builder;

public class ProjectsAvailableAmenitiesDTO {

	private int projectId;
	private int amenitiesAndSpecificationsId;
	private ProjectsDTO project;
	private AmenitiesAndSpecificationsDTO amenitiesAndSpecifications;
	private int projectsAvailableAmenitiesId;

	public int getProjectsAvailableAmenitiesId() {
		return projectsAvailableAmenitiesId;
	}

	public void setProjectsAvailableAmenitiesId(int projectsAvailableAmenitiesId) {
		this.projectsAvailableAmenitiesId = projectsAvailableAmenitiesId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public ProjectsDTO getProject() {
		return project;
	}

	public void setProject(ProjectsDTO project) {
		this.project = project;
	}

	public AmenitiesAndSpecificationsDTO getAmenitiesAndSpecifications() {
		return amenitiesAndSpecifications;
	}

	public void setAmenitiesAndSpecifications(AmenitiesAndSpecificationsDTO amenitiesAndSpecifications) {
		this.amenitiesAndSpecifications = amenitiesAndSpecifications;
	}

	public int getAmenitiesAndSpecificationsId() {
		return amenitiesAndSpecificationsId;
	}

	public void setAmenitiesAndSpecificationsId(int amenitiesAndSpecificationsId) {
		this.amenitiesAndSpecificationsId = amenitiesAndSpecificationsId;
	}

}
