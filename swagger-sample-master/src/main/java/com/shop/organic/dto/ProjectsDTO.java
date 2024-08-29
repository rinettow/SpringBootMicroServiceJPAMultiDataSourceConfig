package com.shop.organic.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.ProjectsAvailableAmenities;

public class ProjectsDTO {

	private int projectId;
	private int builderId;
	private int estimateCost;
	private int areaInSquareFeet;
	private String projMainPicFilePath;
	private BuilderDTO builder;
	private List<PictureDTO> Picture;
	private byte[] image;
	private List<ProjectsAvailableAmenitiesDTO> projectsAvailableAmenities;
	private List<BuildersEstimateDTO> buildersEstimate;


	public List<BuildersEstimateDTO> getBuildersEstimate() {
		return buildersEstimate;
	}

	public void setBuildersEstimate(List<BuildersEstimateDTO> buildersEstimate) {
		this.buildersEstimate = buildersEstimate;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public List<ProjectsAvailableAmenitiesDTO> getProjectsAvailableAmenities() {
		return projectsAvailableAmenities;
	}

	public void setProjectsAvailableAmenities(List<ProjectsAvailableAmenitiesDTO> projectsAvailableAmenities) {
		this.projectsAvailableAmenities = projectsAvailableAmenities;
	}

	public String getProjMainPicFilePath() {
		return projMainPicFilePath;
	}

	public void setProjMainPicFilePath(String projMainPicFilePath) {
		this.projMainPicFilePath = projMainPicFilePath;
	}

	public List<PictureDTO> getPicture() {
		return Picture;
	}

	public void setPicture(List<PictureDTO> picture) {
		Picture = picture;
	}

	public BuilderDTO getBuilder() {
		return builder;
	}

	public void setBuilder(BuilderDTO builder) {
		this.builder = builder;
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
