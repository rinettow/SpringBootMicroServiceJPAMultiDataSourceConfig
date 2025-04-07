package com.shop.organic.entity.car;

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

@Entity
@Table(name = "SUPPLIER_AVAILABLE_CATEGORIES")
public class SupplierAvailableCategories {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUPPLIER_AVAILABLE_CATEGORIES_ID", unique = false, nullable = false)
	private int SupplierAvailableCategoriesId;

	@Column(name = "MATERIAL_SUPPLIER_ID", unique = false, nullable = false)
	private int materialSupplierId;

	@Column(name = "PRODUCT_CATEGORY_ID")
	private int productCategoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_SUPPLIER_ID", insertable = false, updatable = false)
	private MaterialSupplier materialSupplierForSupplierAvailableCategories;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATEGORY_ID", insertable = false, updatable = false)
	private ProductCategory productCategoryForSupplierAvailableCategories;
	
	

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

	public MaterialSupplier getMaterialSupplierForSupplierAvailableCategories() {
		return materialSupplierForSupplierAvailableCategories;
	}

	public void setMaterialSupplierForSupplierAvailableCategories(
			MaterialSupplier materialSupplierForSupplierAvailableCategories) {
		this.materialSupplierForSupplierAvailableCategories = materialSupplierForSupplierAvailableCategories;
	}

	public ProductCategory getProductCategoryForSupplierAvailableCategories() {
		return productCategoryForSupplierAvailableCategories;
	}

	public void setProductCategoryForSupplierAvailableCategories(
			ProductCategory productCategoryForSupplierAvailableCategories) {
		this.productCategoryForSupplierAvailableCategories = productCategoryForSupplierAvailableCategories;
	}



	

}
