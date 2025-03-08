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

public class ProductDTO {

	private int productId;
	private int productSubcategoryId;
	private String productName;
	private String productDescription;
	private String measuremmentUnit;
	private float quantity;
	private ProductSubCategoryDTO subCategoryForProduct;
	private String brandName;
	private String productImagePath;
	private byte[] productImage;
	private List<MaterialRequirementItems> materialRequirementItems;

	
	public int getProductSubcategoryId() {
		return productSubcategoryId;
	}

	public void setProductSubcategoryId(int productSubcategoryId) {
		this.productSubcategoryId = productSubcategoryId;
	}

	public List<MaterialRequirementItems> getMaterialRequirementItems() {
		return materialRequirementItems;
	}

	public void setMaterialRequirementItems(List<MaterialRequirementItems> materialRequirementItems) {
		this.materialRequirementItems = materialRequirementItems;
	}

	public byte[] getProductImage() {
		return productImage;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	public ProductSubCategoryDTO getSubCategoryForProduct() {
		return subCategoryForProduct;
	}

	public void setSubCategoryForProduct(ProductSubCategoryDTO subCategoryForProduct) {
		this.subCategoryForProduct = subCategoryForProduct;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getMeasuremmentUnit() {
		return measuremmentUnit;
	}

	public void setMeasuremmentUnit(String measuremmentUnit) {
		this.measuremmentUnit = measuremmentUnit;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductImagePath() {
		return productImagePath;
	}

	public void setProductImagePath(String productImagePath) {
		this.productImagePath = productImagePath;
	}
	

	
	

}
