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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "PRODUCT")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID")
	private int productId;
	
	@Column(name = "PRODUCT_SUB_CATEGORY_ID")
	private int productSubcategoryId;

	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Column(name = "PRODUCT_DESCRIPTION")
	private String productDescription;
	
	@Column(name = "MEASUREMENT_UNIT")
	private String measuremmentUnit;
	
	@Column(name = "QUANTITY")
	private float quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_SUB_CATEGORY_ID", insertable = false, updatable = false)
	private ProductSubCategory subCategoryForProduct;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productForMaterialRequirementItems")
	private List<MaterialRequirementItems> materialRequirementItems;
	
	
	@Column(name = "BRAND_NAME")
	private String brandName;
	
	@Column(name = "PRODUCT_IMAGE_PATH")
	private String productImagePath;

	
	
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

	public ProductSubCategory getSubCategoryForProduct() {
		return subCategoryForProduct;
	}

	public void setSubCategoryForProduct(ProductSubCategory subCategoryForProduct) {
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
