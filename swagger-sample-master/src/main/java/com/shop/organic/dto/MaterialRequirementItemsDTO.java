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
import com.shop.organic.entity.car.MaterialRequirementItemsEstimate;

public class MaterialRequirementItemsDTO {

	
	private int materialRequirementItemsId;
	private int materialRequirementId;
	private int productId;
	private int productSubcategoryId;
	private MaterialRequirementDTO materialRequirementForMaterialRequirementItems;
	private ProductDTO productForMaterialRequirementItems;
	private ProductSubCategoryDTO productSubCategoryForMaterialRequirementItems;
	private List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate;
	private int quantity;
	private float bestPrice;
	private MaterialSupplierDTO bestPriceMaterialSupplier;
	
	
	
	
	public MaterialSupplierDTO getBestPriceMaterialSupplier() {
		return bestPriceMaterialSupplier;
	}
	public void setBestPriceMaterialSupplier(MaterialSupplierDTO bestPriceMaterialSupplier) {
		this.bestPriceMaterialSupplier = bestPriceMaterialSupplier;
	}
	public float getBestPrice() {
		return bestPrice;
	}
	public void setBestPrice(float bestPrice) {
		this.bestPrice = bestPrice;
	}
	public int getMaterialRequirementId() {
		return materialRequirementId;
	}
	public void setMaterialRequirementId(int materialRequirementId) {
		this.materialRequirementId = materialRequirementId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductSubcategoryId() {
		return productSubcategoryId;
	}
	public void setProductSubcategoryId(int productSubcategoryId) {
		this.productSubcategoryId = productSubcategoryId;
	}
	public List<MaterialRequirementItemsEstimateDTO> getMaterialRequirementItemsEstimate() {
		return materialRequirementItemsEstimate;
	}
	public void setMaterialRequirementItemsEstimate(
			List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate) {
		this.materialRequirementItemsEstimate = materialRequirementItemsEstimate;
	}
	public int getMaterialRequirementItemsId() {
		return materialRequirementItemsId;
	}
	public void setMaterialRequirementItemsId(int materialRequirementItemsId) {
		this.materialRequirementItemsId = materialRequirementItemsId;
	}
	public MaterialRequirementDTO getMaterialRequirementForMaterialRequirementItems() {
		return materialRequirementForMaterialRequirementItems;
	}
	public void setMaterialRequirementForMaterialRequirementItems(
			MaterialRequirementDTO materialRequirementForMaterialRequirementItems) {
		this.materialRequirementForMaterialRequirementItems = materialRequirementForMaterialRequirementItems;
	}
	public ProductDTO getProductForMaterialRequirementItems() {
		return productForMaterialRequirementItems;
	}
	public void setProductForMaterialRequirementItems(ProductDTO productForMaterialRequirementItems) {
		this.productForMaterialRequirementItems = productForMaterialRequirementItems;
	}
	public ProductSubCategoryDTO getProductSubCategoryForMaterialRequirementItems() {
		return productSubCategoryForMaterialRequirementItems;
	}
	public void setProductSubCategoryForMaterialRequirementItems(
			ProductSubCategoryDTO productSubCategoryForMaterialRequirementItems) {
		this.productSubCategoryForMaterialRequirementItems = productSubCategoryForMaterialRequirementItems;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	
		

}
