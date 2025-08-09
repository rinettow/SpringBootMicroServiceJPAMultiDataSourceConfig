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
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_CATEGORY_ID")
	private int productCategoryId;

	@Column(name = "PRODUCT_CATEGORY_NAME")
	private String productCategoryName;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryForSubCaegory")
	private List<ProductSubCategory> productSubCategory;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryForBrand")
	private List<ProductBrand> productBrand;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryForMaterialRequirement")
	private List<MaterialRequirement> materialRequirement;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productCategoryForSupplierAvailableCategories")
	private List<SupplierAvailableCategories> supplierAvailableCategories;

	
	public List<ProductBrand> getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(List<ProductBrand> productBrand) {
		this.productBrand = productBrand;
	}

	public List<SupplierAvailableCategories> getSupplierAvailableCategories() {
		return supplierAvailableCategories;
	}

	public void setSupplierAvailableCategories(List<SupplierAvailableCategories> supplierAvailableCategories) {
		this.supplierAvailableCategories = supplierAvailableCategories;
	}

	public List<MaterialRequirement> getMaterialRequirement() {
		return materialRequirement;
	}

	public void setMaterialRequirement(List<MaterialRequirement> materialRequirement) {
		this.materialRequirement = materialRequirement;
	}

	public List<ProductSubCategory> getProductSubCategory() {
		return productSubCategory;
	}

	public void setProductSubCategory(List<ProductSubCategory> productSubCategory) {
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
