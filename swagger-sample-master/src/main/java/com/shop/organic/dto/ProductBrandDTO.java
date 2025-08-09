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


public class ProductBrandDTO {

	private int productBrandId;
	private String productBrandName;
	private ProductCategoryDTO categoryForBrand;
	private List<SupplierAvailableBrandsDTO> materialSupplierAvailableBrands;
	

	public int getProductBrandId() {
		return productBrandId;
	}

	public void setProductBrandId(int productBrandId) {
		this.productBrandId = productBrandId;
	}

	public String getProductBrandName() {
		return productBrandName;
	}

	public void setProductBrandName(String productBrandName) {
		this.productBrandName = productBrandName;
	}

	public ProductCategoryDTO getCategoryForBrand() {
		return categoryForBrand;
	}

	public void setCategoryForBrand(ProductCategoryDTO categoryForBrand) {
		this.categoryForBrand = categoryForBrand;
	}

	

	
	

}
