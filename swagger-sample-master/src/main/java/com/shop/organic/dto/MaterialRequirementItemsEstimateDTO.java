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


public class MaterialRequirementItemsEstimateDTO {

	
	private int materialRequirementItemEstmtimateId;
	private MaterialRequirementItemsDTO materialRequirementItemsForMaterialRequirementItemsEstimate;
	private MaterialRequirementDTO materialRequirementForMaterialRequirementItemsEstimate;
	private MaterialSupplierDTO materialSupplierForMaterialRequirementItemsEstimate;
	private float totalPrice;
	
	public int getMaterialRequirementItemEstmtimateId() {
		return materialRequirementItemEstmtimateId;
	}
	public void setMaterialRequirementItemEstmtimateId(int materialRequirementItemEstmtimateId) {
		this.materialRequirementItemEstmtimateId = materialRequirementItemEstmtimateId;
	}
	public MaterialRequirementItemsDTO getMaterialRequirementItemsForMaterialRequirementItemsEstimate() {
		return materialRequirementItemsForMaterialRequirementItemsEstimate;
	}
	public void setMaterialRequirementItemsForMaterialRequirementItemsEstimate(
			MaterialRequirementItemsDTO materialRequirementItemsForMaterialRequirementItemsEstimate) {
		this.materialRequirementItemsForMaterialRequirementItemsEstimate = materialRequirementItemsForMaterialRequirementItemsEstimate;
	}
	public MaterialRequirementDTO getMaterialRequirementForMaterialRequirementItemsEstimate() {
		return materialRequirementForMaterialRequirementItemsEstimate;
	}
	public void setMaterialRequirementForMaterialRequirementItemsEstimate(
			MaterialRequirementDTO materialRequirementForMaterialRequirementItemsEstimate) {
		this.materialRequirementForMaterialRequirementItemsEstimate = materialRequirementForMaterialRequirementItemsEstimate;
	}
	public MaterialSupplierDTO getMaterialSupplierForMaterialRequirementItemsEstimate() {
		return materialSupplierForMaterialRequirementItemsEstimate;
	}
	public void setMaterialSupplierForMaterialRequirementItemsEstimate(
			MaterialSupplierDTO materialSupplierForMaterialRequirementItemsEstimate) {
		this.materialSupplierForMaterialRequirementItemsEstimate = materialSupplierForMaterialRequirementItemsEstimate;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}


	
		

}
