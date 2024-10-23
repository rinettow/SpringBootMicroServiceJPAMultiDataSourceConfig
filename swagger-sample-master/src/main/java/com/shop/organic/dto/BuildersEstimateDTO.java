package com.shop.organic.dto;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class BuildersEstimateDTO {

	private int buildersEstimateId;
	private int customerRequirementId;
	private int projectId;
	private int builderId;
	private float perSquareFeetCost;
	private String detailedEstimateFilePath;
	private String customerAcceptedDeclined;
	private String customerReview;
	private int customerReviewStarRating;
	private CustomerRequirementDTO customerRequirementDTO;
	private ProjectsDTO projectDTO;
	private BuilderDTO builderDTO;
	private byte[] detailedEstimateFile;
	
	
	
	public int getCustomerReviewStarRating() {
		return customerReviewStarRating;
	}
	public void setCustomerReviewStarRating(int customerReviewStarRating) {
		this.customerReviewStarRating = customerReviewStarRating;
	}
	public byte[] getDetailedEstimateFile() {
		return detailedEstimateFile;
	}
	public void setDetailedEstimateFile(byte[] detailedEstimateFile) {
		this.detailedEstimateFile = detailedEstimateFile;
	}
	
	public String getCustomerReview() {
		return customerReview;
	}
	public void setCustomerReview(String customerReview) {
		this.customerReview = customerReview;
	}
	public int getBuildersEstimateId() {
		return buildersEstimateId;
	}
	public String getCustomerAcceptedDeclined() {
		return customerAcceptedDeclined;
	}
	public void setCustomerAcceptedDeclined(String customerAcceptedDeclined) {
		this.customerAcceptedDeclined = customerAcceptedDeclined;
	}
	public void setBuildersEstimateId(int buildersEstimateId) {
		this.buildersEstimateId = buildersEstimateId;
	}
	
	public int getCustomerRequirementId() {
		return customerRequirementId;
	}
	public void setCustomerRequirementId(int customerRequirementId) {
		this.customerRequirementId = customerRequirementId;
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
	public float getPerSquareFeetCost() {
		return perSquareFeetCost;
	}
	public void setPerSquareFeetCost(float perSquareFeetCost) {
		this.perSquareFeetCost = perSquareFeetCost;
	}
	public String getDetailedEstimateFilePath() {
		return detailedEstimateFilePath;
	}
	public void setDetailedEstimateFilePath(String detailedEstimateFilePath) {
		this.detailedEstimateFilePath = detailedEstimateFilePath;
	}
	public CustomerRequirementDTO getCustomerRequirementDTO() {
		return customerRequirementDTO;
	}
	public void setCustomerRequirementDTO(CustomerRequirementDTO customerRequirementDTO) {
		this.customerRequirementDTO = customerRequirementDTO;
	}
	public ProjectsDTO getProjectDTO() {
		return projectDTO;
	}
	public void setProjectDTO(ProjectsDTO projectDTO) {
		this.projectDTO = projectDTO;
	}
	public BuilderDTO getBuilderDTO() {
		return builderDTO;
	}
	public void setBuilderDTO(BuilderDTO builderDTO) {
		this.builderDTO = builderDTO;
	}

	
}
