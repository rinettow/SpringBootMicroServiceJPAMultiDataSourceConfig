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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "PROJECTS")
public class Projects {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROJECT_ID", insertable = false, updatable = false)
	private int projectId;

	@Column(name = "BUILDER_ID")
	private int builderId;

	@Column(name = "ESTIMATE_COST")
	private int estimateCost;

	@Column(name = "AREA_IN_SQARE_FEET")
	private int areaInSquareFeet;

	@Column(name = "PROJ_MAIN_PIC_FILE_PATH")
	private String projMainPicFilePath;
	
	@Column(name = "PROJ_MAIN_VIDEO_FILE_PATH")
	private String projMainVideoFilePath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDER_ID", insertable = false, updatable = false)
	private Builder builderForProjects;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectForAmenity")
	private List<ProjectsAvailableAmenities> projectsAvailableAmenities;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectForPicture")
	private List<Picture> Picture;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectForBuildersEstimate")
	private List<BuildersEstimate> buildersEstimate;

	public List<ProjectsAvailableAmenities> getProjectsAvailableAmenities() {
		return projectsAvailableAmenities;
	}

	public void setProjectsAvailableAmenities(List<ProjectsAvailableAmenities> projectsAvailableAmenities) {
		this.projectsAvailableAmenities = projectsAvailableAmenities;
	}

	public String getProjMainPicFilePath() {
		return projMainPicFilePath;
	}
	
	public String getProjMainVideoFilePath() {
		return projMainVideoFilePath;
	}

	public void setProjMainVideoFilePath(String projMainVideoFilePath) {
		this.projMainVideoFilePath = projMainVideoFilePath;
	}


	public void setProjMainPicFilePath(String projMainPicFilePath) {
		this.projMainPicFilePath = projMainPicFilePath;
	}

	public List<Picture> getPicture() {
		return Picture;
	}

	public List<BuildersEstimate> getBuildersEstimate() {
		return buildersEstimate;
	}

	public void setBuildersEstimate(List<BuildersEstimate> buildersEstimate) {
		this.buildersEstimate = buildersEstimate;
	}

	public void setPicture(List<Picture> picture) {
		Picture = picture;
	}

	public Builder getBuilderForProjects() {
		return builderForProjects;
	}

	public void setBuilderForProjects(Builder builderForProjects) {
		this.builderForProjects = builderForProjects;
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
