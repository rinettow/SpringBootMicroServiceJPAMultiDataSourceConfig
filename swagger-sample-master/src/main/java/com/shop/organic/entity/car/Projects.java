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
import javax.persistence.Table;


@Entity
@Table(name = "PROJECTS")
public class Projects {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROJECT_ID", insertable = false, updatable = false)
	 private int projectId;
	
	@Column(name = "BUILDER_ID")
    private int builderId;
   
	@Column(name = "AMENITIES_AND_SPECIFICATIONS_ID")
    private int amenitiesAndSpecificationsId;
   
	@Column(name = "ESTIMATE_COST")
    private int estimateCost;
   
	@Column(name = "AREA_IN_SQARE_FEET")
    private int areaInSquareFeet;
	
	
	@Column(name = "PROJ_MAIN_PIC_FILE_PATH")
    private String projMainPicFilePath;
	

	@ManyToOne
	@JoinColumn(name="BUILDER_ID", insertable = false, updatable = false)
	private Builder builder;
	
	@ManyToOne
    @JoinColumn(name="AMENITIES_AND_SPECIFICATIONS_ID", insertable = false, updatable = false)
	private AmenitiesAndSpecifications amenitiesAndSpecifications;
	
	@OneToMany(mappedBy="projects", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Picture> Picture;
	
	
	
	public String getProjMainPicFilePath() {
		return projMainPicFilePath;
	}

	public void setProjMainPicFilePath(String projMainPicFilePath) {
		this.projMainPicFilePath = projMainPicFilePath;
	}
	
	public List<Picture> getPicture() {
		return Picture;
	}

	public void setPicture(List<Picture> picture) {
		Picture = picture;
	}

	public Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Builder builder) {
		this.builder = builder;
	}

	public AmenitiesAndSpecifications getAmenitiesAndSpecifications() {
		return amenitiesAndSpecifications;
	}

	public void setAmenitiesAndSpecifications(AmenitiesAndSpecifications amenitiesAndSpecifications) {
		this.amenitiesAndSpecifications = amenitiesAndSpecifications;
	}

	
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

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

	public int getEstimateCost() {
		return estimateCost;
	}

	public void setEstimateCost(int estimateCost) {
		this.estimateCost = estimateCost;
	}

	public int getAreaInSquareFeet() {
		return areaInSquareFeet;
	}

	public void setAreaInSquareFeet(int areaInSquareFeet) {
		this.areaInSquareFeet = areaInSquareFeet;
	}

	

}
