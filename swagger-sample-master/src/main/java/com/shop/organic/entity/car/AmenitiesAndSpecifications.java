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
	
	@OneToMany(mappedBy="amenitiesAndSpecifications", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Projects> projects;
	
	@OneToMany(mappedBy="amenitiesAndSpecifications", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Builder> builder;
	

	@OneToMany(mappedBy="amenitiesAndSpecifications")
    private Set<BuildersAvailableAmenities> buildersAvailableAmenities;
	
		
	
	public List<Builder> getBuilder() {
		return builder;
	}

	public void setBuilder(List<Builder> builder) {
		this.builder = builder;
	}
	
	public List<Projects> getProjects() {
		return projects;
	}

	public void setProjects(List<Projects> projects) {
		this.projects = projects;
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
