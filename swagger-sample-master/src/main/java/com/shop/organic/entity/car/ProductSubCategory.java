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
@Table(name = "PRODUCT_SUB_CATEGORY")
public class ProductSubCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_SUB_CATEGORY_ID")
	private int productSubCategoryId;

	@Column(name = "PRODUCT_SUB_CATEGORY_NAME")
	private String productSubCategoryName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATEGORY_ID", insertable = false, updatable = false)
	private ProductCategory categoryForSubCaegory;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subCategoryForProduct")
	private List<Product> product;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productSubCategoryForMaterialRequirementItems")
	private List<MaterialRequirementItems> materialRequirementItems;

	
	public List<MaterialRequirementItems> getMaterialRequirementItems() {
		return materialRequirementItems;
	}

	public void setMaterialRequirementItems(List<MaterialRequirementItems> materialRequirementItems) {
		this.materialRequirementItems = materialRequirementItems;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}

	public ProductCategory getCategoryForSubCaegory() {
		return categoryForSubCaegory;
	}

	public void setCategoryForSubCaegory(ProductCategory categoryForSubCaegory) {
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
