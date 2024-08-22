package com.shop.organic.entity.car;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.shop.organic.entity.car.Builder;

@Entity
@Table(name = "PROJECTS_AVAILABLE_AMENITIES")
public class ProjectsAvailableAmenities {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROJECTS_AVAILABLE_AMENITIES_ID", unique = false, nullable = false)
	private int projectsAvailableAmenitiesId;

	@Column(name = "PROJECT_ID", unique = false, nullable = false)
	private int projectId;

	@Column(name = "AMENITIES_AND_SPECIFICATIONS_ID")
	private int amenitiesAndSpecificationsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_ID", insertable = false, updatable = false)
	private Projects projectForAmenity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AMENITIES_AND_SPECIFICATIONS_ID", insertable = false, updatable = false)
	private AmenitiesAndSpecifications amenitiesAndSpecificationsForProjects;

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getProjectsAvailableAmenitiesId() {
		return projectsAvailableAmenitiesId;
	}

	public void setProjectsAvailableAmenitiesId(int projectsAvailableAmenitiesId) {
		this.projectsAvailableAmenitiesId = projectsAvailableAmenitiesId;
	}

	public Projects getProjectForAmenity() {
		return projectForAmenity;
	}

	public void setProjectForAmenity(Projects projectForAmenity) {
		this.projectForAmenity = projectForAmenity;
	}

	public AmenitiesAndSpecifications getAmenitiesAndSpecificationsForProjects() {
		return amenitiesAndSpecificationsForProjects;
	}

	public void setAmenitiesAndSpecificationsForProjects(
			AmenitiesAndSpecifications amenitiesAndSpecificationsForProjects) {
		this.amenitiesAndSpecificationsForProjects = amenitiesAndSpecificationsForProjects;
	}

	public int getAmenitiesAndSpecificationsId() {
		return amenitiesAndSpecificationsId;
	}

	public void setAmenitiesAndSpecificationsId(int amenitiesAndSpecificationsId) {
		this.amenitiesAndSpecificationsId = amenitiesAndSpecificationsId;
	}

}
