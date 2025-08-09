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
@Table(name = "PRODUCT_BRAND")
public class ProductBrand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_BRAND_ID")
	private int productBrandId;

	@Column(name = "PRODUCT_BRAND_NAME")
	private String productBrandName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATEGORY_ID", insertable = false, updatable = false)
	private ProductCategory categoryForBrand;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productBrandForSupplierAvailableBrands")
	private List<SupplierAvailableBrands> materialSupplierAvailableBrands;
	
	

	public List<SupplierAvailableBrands> getMaterialSupplierAvailableBrands() {
		return materialSupplierAvailableBrands;
	}

	public void setMaterialSupplierAvailableBrands(List<SupplierAvailableBrands> materialSupplierAvailableBrands) {
		this.materialSupplierAvailableBrands = materialSupplierAvailableBrands;
	}

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

	public ProductCategory getCategoryForBrand() {
		return categoryForBrand;
	}

	public void setCategoryForBrand(ProductCategory categoryForBrand) {
		this.categoryForBrand = categoryForBrand;
	}

	
	

}
