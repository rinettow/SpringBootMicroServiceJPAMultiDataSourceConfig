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
@Table(name = "SUPPLIER_AVAILABLE_BRANDS")
public class SupplierAvailableBrands {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUPPLIER_AVAILABLE_BRANDS_ID", unique = false, nullable = false)
	private int SupplierAvailableBrandId;

	@Column(name = "MATERIAL_SUPPLIER_ID", unique = false, nullable = false)
	private int materialSupplierId;

	@Column(name = "PRODUCT_BRAND_ID")
	private int productBrandId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_SUPPLIER_ID", insertable = false, updatable = false)
	private MaterialSupplier materialSupplierForSupplierAvailableBrands;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_BRAND_ID", insertable = false, updatable = false)
	private ProductBrand productBrandForSupplierAvailableBrands;

	public int getSupplierAvailableBrandId() {
		return SupplierAvailableBrandId;
	}

	public void setSupplierAvailableBrandId(int supplierAvailableBrandId) {
		SupplierAvailableBrandId = supplierAvailableBrandId;
	}

	public int getMaterialSupplierId() {
		return materialSupplierId;
	}

	public void setMaterialSupplierId(int materialSupplierId) {
		this.materialSupplierId = materialSupplierId;
	}

	public int getProductBrandId() {
		return productBrandId;
	}

	public void setProductBrandId(int productBrandId) {
		this.productBrandId = productBrandId;
	}

	public MaterialSupplier getMaterialSupplierForSupplierAvailableBrands() {
		return materialSupplierForSupplierAvailableBrands;
	}

	public void setMaterialSupplierForSupplierAvailableBrands(MaterialSupplier materialSupplierForSupplierAvailableBrands) {
		this.materialSupplierForSupplierAvailableBrands = materialSupplierForSupplierAvailableBrands;
	}

	public ProductBrand getProductBrandForSupplierAvailableBrands() {
		return productBrandForSupplierAvailableBrands;
	}

	public void setProductBrandForSupplierAvailableBrands(ProductBrand productBrandForSupplierAvailableBrands) {
		this.productBrandForSupplierAvailableBrands = productBrandForSupplierAvailableBrands;
	}
	
	

	
	

}
