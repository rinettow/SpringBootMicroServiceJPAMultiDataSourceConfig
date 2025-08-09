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

public class SupplierAvailableBrandsDTO {
	
	private int SupplierAvailableBrandId;
	private int materialSupplierId;
	private int productBrandId;
	private MaterialSupplierDTO materialSupplierForSupplierAvailableBrands;
	private ProductBrandDTO productBrandForSupplierAvailableBrands;

	
	public void setMaterialSupplierForSupplierAvailableBrands(
			MaterialSupplierDTO materialSupplierForSupplierAvailableBrands) {
		this.materialSupplierForSupplierAvailableBrands = materialSupplierForSupplierAvailableBrands;
	}

	public void setProductBrandForSupplierAvailableBrands(ProductBrandDTO productBrandForSupplierAvailableBrands) {
		this.productBrandForSupplierAvailableBrands = productBrandForSupplierAvailableBrands;
	}

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

	public MaterialSupplierDTO getMaterialSupplierForSupplierAvailableBrands() {
		return materialSupplierForSupplierAvailableBrands;
	}

	public ProductBrandDTO getProductBrandForSupplierAvailableBrands() {
		return productBrandForSupplierAvailableBrands;
	}



	
	

	
	

}
