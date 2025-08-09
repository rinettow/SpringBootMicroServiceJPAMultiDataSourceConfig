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
import com.shop.organic.entity.car.MaterialRequirement;
import com.shop.organic.entity.car.ProductBrand;


public class ProductCategoryDTO {

	private int productCategoryId;
    private String productCategoryName;
	private List<ProductSubCategoryDTO> productSubCategory;
	private List<MaterialRequirementDTO> materialRequirement;
	private List<ProductBrandDTO> productBrand;
	
	
	

	public List<MaterialRequirementDTO> getMaterialRequirement() {
		return materialRequirement;
	}

	public void setMaterialRequirement(List<MaterialRequirementDTO> materialRequirement) {
		this.materialRequirement = materialRequirement;
	}

	public List<ProductBrandDTO> getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(List<ProductBrandDTO> productBrand) {
		this.productBrand = productBrand;
	}

	public List<ProductSubCategoryDTO> getProductSubCategory() {
		return productSubCategory;
	}

	public void setProductSubCategory(List<ProductSubCategoryDTO> productSubCategory) {
		this.productSubCategory = productSubCategory;
	}

	public int getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(int productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	

}
