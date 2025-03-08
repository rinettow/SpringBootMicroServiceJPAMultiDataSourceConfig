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

public class ProductSubCategoryDTO {

	private int productSubCategoryId;
	private String productSubCategoryName;
	private ProductCategoryDTO categoryForSubCaegory;
	private List<ProductDTO> product;
	private List<MaterialRequirementItems> materialRequirementItems;
	
	
	
	public List<MaterialRequirementItems> getMaterialRequirementItems() {
		return materialRequirementItems;
	}

	public void setMaterialRequirementItems(List<MaterialRequirementItems> materialRequirementItems) {
		this.materialRequirementItems = materialRequirementItems;
	}

	public List<ProductDTO> getProduct() {
		return product;
	}

	public void setProduct(List<ProductDTO> product) {
		this.product = product;
	}

	public ProductCategoryDTO getCategoryForSubCaegory() {
		return categoryForSubCaegory;
	}

	public void setCategoryForSubCaegory(ProductCategoryDTO categoryForSubCaegory) {
		this.categoryForSubCaegory = categoryForSubCaegory;
	}

	public int getProductSubCategoryId() {
		return productSubCategoryId;
	}

	public void setProductSubCategoryId(int productSubCategoryId) {
		this.productSubCategoryId = productSubCategoryId;
	}

	public String getProductSubCategoryName() {
		return productSubCategoryName;
	}

	public void setProductSubCategoryName(String productSubCategoryName) {
		this.productSubCategoryName = productSubCategoryName;
	}


	

}
