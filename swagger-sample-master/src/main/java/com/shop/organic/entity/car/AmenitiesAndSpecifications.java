package com.shop.organic.entity.car;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "AMENITIES_AND_SPECIFICATIONS")
public class AmenitiesAndSpecifications {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AMENITIES_AND_SPECIFICATIONS_ID", insertable = false, updatable = false)
	private int amenitiesAndSpecificationsId;

	@Column(name = "AMENITIES_AND_SPECIFICATIONS_NAME")
	private String amenitiesAndSpecificationsName;

	@OneToMany(mappedBy = "amenitiesAndSpecificationsForBuilders")
	private List<BuildersAvailableAmenities> buildersAvailableAmenities;

	@OneToMany(mappedBy = "amenitiesAndSpecificationsForProjects")
	private List<ProjectsAvailableAmenities> projectsAvailableAmenities;

	@OneToMany(mappedBy = "amenitiesAndSpecificationsForCustomerRequirement")
	private List<CustomerRequirement> customerRequirement;

	public List<BuildersAvailableAmenities> getBuildersAvailableAmenities() {
		return buildersAvailableAmenities;
	}

	public void setBuildersAvailableAmenities(List<BuildersAvailableAmenities> buildersAvailableAmenities) {
		this.buildersAvailableAmenities = buildersAvailableAmenities;
	}

	public List<ProjectsAvailableAmenities> getProjectsAvailableAmenities() {
		return projectsAvailableAmenities;
	}

	public void setProjectsAvailableAmenities(List<ProjectsAvailableAmenities> projectsAvailableAmenities) {
		this.projectsAvailableAmenities = projectsAvailableAmenities;
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
