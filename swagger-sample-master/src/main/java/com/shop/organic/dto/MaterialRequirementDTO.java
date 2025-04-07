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
import com.shop.organic.entity.car.MaterialRequirementItems;

public class MaterialRequirementDTO {

	
	private int materialRequirementId;
    private Integer customerId= null;
	private Integer builderId= null;
	private int productCategoryId;
	private CustomerDTO customerForMaterialRequirement;
	private BuilderDTO builderForMaterialRequirement;
	private ProductCategoryDTO categoryForMaterialRequirement;
	private String requirementStatus;
	private String state;
	private String district;
	private List<MaterialRequirementItemsDTO> materialRequirementItems;
	private List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate;
	
	
	
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getBuilderId() {
		return builderId;
	}
	public void setBuilderId(Integer builderId) {
		this.builderId = builderId;
	}
	public int getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(int productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public List<MaterialRequirementItemsDTO> getMaterialRequirementItems() {
		return materialRequirementItems;
	}
	public void setMaterialRequirementItems(List<MaterialRequirementItemsDTO> materialRequirementItems) {
		this.materialRequirementItems = materialRequirementItems;
	}
	public List<MaterialRequirementItemsEstimateDTO> getMaterialRequirementItemsEstimate() {
		return materialRequirementItemsEstimate;
	}
	public void setMaterialRequirementItemsEstimate(
			List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate) {
		this.materialRequirementItemsEstimate = materialRequirementItemsEstimate;
	}
	public int getMaterialRequirementId() {
		return materialRequirementId;
	}
	public void setMaterialRequirementId(int materialRequirementId) {
		this.materialRequirementId = materialRequirementId;
	}
	public CustomerDTO getCustomerForMaterialRequirement() {
		return customerForMaterialRequirement;
	}
	public void setCustomerForMaterialRequirement(CustomerDTO customerForMaterialRequirement) {
		this.customerForMaterialRequirement = customerForMaterialRequirement;
	}
	public BuilderDTO getBuilderForMaterialRequirement() {
		return builderForMaterialRequirement;
	}
	public void setBuilderForMaterialRequirement(BuilderDTO builderForMaterialRequirement) {
		this.builderForMaterialRequirement = builderForMaterialRequirement;
	}
	public ProductCategoryDTO getCategoryForMaterialRequirement() {
		return categoryForMaterialRequirement;
	}
	public void setCategoryForMaterialRequirement(ProductCategoryDTO categoryForMaterialRequirement) {
		this.categoryForMaterialRequirement = categoryForMaterialRequirement;
	}
	public String getRequirementStatus() {
		return requirementStatus;
	}
	public void setRequirementStatus(String requirementStatus) {
		this.requirementStatus = requirementStatus;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}

	
}
