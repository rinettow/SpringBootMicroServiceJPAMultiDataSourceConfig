package com.shop.organic.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.organic.entity.car.Builder;

public class SupplierAvailableCategoriesDTO {
	private int SupplierAvailableCategoriesId;
	private int materialSupplierId;
	private int productCategoryId;
	private MaterialSupplierDTO materialSupplierForSupplierAvailableCategories;
	private ProductCategoryDTO productCategoryForSupplierAvailableCategories;
	
	
	public int getSupplierAvailableCategoriesId() {
		return SupplierAvailableCategoriesId;
	}
	public void setSupplierAvailableCategoriesId(int supplierAvailableCategoriesId) {
		SupplierAvailableCategoriesId = supplierAvailableCategoriesId;
	}
	public int getMaterialSupplierId() {
		return materialSupplierId;
	}
	public void setMaterialSupplierId(int materialSupplierId) {
		this.materialSupplierId = materialSupplierId;
	}
	public int getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(int productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public MaterialSupplierDTO getMaterialSupplierForSupplierAvailableCategories() {
		return materialSupplierForSupplierAvailableCategories;
	}
	public void setMaterialSupplierForSupplierAvailableCategories(
			MaterialSupplierDTO materialSupplierForSupplierAvailableCategories) {
		this.materialSupplierForSupplierAvailableCategories = materialSupplierForSupplierAvailableCategories;
	}
	public ProductCategoryDTO getProductCategoryForSupplierAvailableCategories() {
		return productCategoryForSupplierAvailableCategories;
	}
	public void setProductCategoryForSupplierAvailableCategories(
			ProductCategoryDTO productCategoryForSupplierAvailableCategories) {
		this.productCategoryForSupplierAvailableCategories = productCategoryForSupplierAvailableCategories;
	}
	

}
