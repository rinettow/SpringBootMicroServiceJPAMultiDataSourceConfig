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
import com.shop.organic.entity.car.SupplierAvailableBrands;
import com.shop.organic.entity.car.SupplierAvailableCategories;

public class MaterialSupplierDTO {

	private int materialSupplierBuilderId;
	private String materialSupplierBuilderName;
	private String materialSupplierCompany;
	private String materialSupplierPhone;
	private String materialSupplierUserName;
	private String materialSupplierPassword;
	private MaterialSupplierAddressDTO materialSupplierAddress;
	private List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate;
	private List<SupplierAvailableCategoriesDTO> materialSupplierAvailableCategories;
	private List<MaterialRequirementDTO> materialRequirement;
	private BuilderDTO builderDTO;
	private List<SupplierAvailableBrandsDTO> materialSupplierAvailableBrands;
	
	
	
	public List<SupplierAvailableBrandsDTO> getMaterialSupplierAvailableBrands() {
		return materialSupplierAvailableBrands;
	}
	public void setMaterialSupplierAvailableBrands(List<SupplierAvailableBrandsDTO> materialSupplierAvailableBrands) {
		this.materialSupplierAvailableBrands = materialSupplierAvailableBrands;
	}
	public List<MaterialRequirementDTO> getMaterialRequirement() {
		return materialRequirement;
	}
	public void setMaterialRequirement(List<MaterialRequirementDTO> materialRequirement) {
		this.materialRequirement = materialRequirement;
	}
	public BuilderDTO getBuilderDTO() {
		return builderDTO;
	}
	public void setBuilderDTO(BuilderDTO builderDTO) {
		this.builderDTO = builderDTO;
	}
	
	public List<SupplierAvailableCategoriesDTO> getMaterialSupplierAvailableCategories() {
		return materialSupplierAvailableCategories;
	}
	public void setMaterialSupplierAvailableCategories(
			List<SupplierAvailableCategoriesDTO> materialSupplierAvailableCategories) {
		this.materialSupplierAvailableCategories = materialSupplierAvailableCategories;
	}
	public List<MaterialRequirementItemsEstimateDTO> getMaterialRequirementItemsEstimate() {
		return materialRequirementItemsEstimate;
	}
	public void setMaterialRequirementItemsEstimate(
			List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate) {
		this.materialRequirementItemsEstimate = materialRequirementItemsEstimate;
	}
	public int getMaterialSupplierBuilderId() {
		return materialSupplierBuilderId;
	}
	public void setMaterialSupplierBuilderId(int materialSupplierBuilderId) {
		this.materialSupplierBuilderId = materialSupplierBuilderId;
	}
	public String getMaterialSupplierBuilderName() {
		return materialSupplierBuilderName;
	}
	public void setMaterialSupplierBuilderName(String materialSupplierBuilderName) {
		this.materialSupplierBuilderName = materialSupplierBuilderName;
	}
	public String getMaterialSupplierCompany() {
		return materialSupplierCompany;
	}
	public void setMaterialSupplierCompany(String materialSupplierCompany) {
		this.materialSupplierCompany = materialSupplierCompany;
	}
	public String getMaterialSupplierPhone() {
		return materialSupplierPhone;
	}
	public void setMaterialSupplierPhone(String materialSupplierPhone) {
		this.materialSupplierPhone = materialSupplierPhone;
	}
	public String getMaterialSupplierUserName() {
		return materialSupplierUserName;
	}
	public void setMaterialSupplierUserName(String materialSupplierUserName) {
		this.materialSupplierUserName = materialSupplierUserName;
	}
	public String getMaterialSupplierPassword() {
		return materialSupplierPassword;
	}
	public void setMaterialSupplierPassword(String materialSupplierPassword) {
		this.materialSupplierPassword = materialSupplierPassword;
	}
	public MaterialSupplierAddressDTO getMaterialSupplierAddress() {
		return materialSupplierAddress;
	}
	public void setMaterialSupplierAddress(MaterialSupplierAddressDTO materialSupplierAddress) {
		this.materialSupplierAddress = materialSupplierAddress;
	}

	
}
