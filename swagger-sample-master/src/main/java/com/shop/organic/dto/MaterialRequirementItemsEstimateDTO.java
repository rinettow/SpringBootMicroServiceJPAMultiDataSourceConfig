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
    private int materialRequirementItemId;
	private int materialRequirementId;
	private int materialSupplierId;
	private MaterialRequirementItemsDTO materialRequirementItemsForMaterialRequirementItemsEstimate;
	private MaterialRequirementDTO materialRequirementForMaterialRequirementItemsEstimate;
	private MaterialSupplierDTO materialSupplierForMaterialRequirementItemsEstimate;
	private float totalPrice;
	private String customerBuilderAceptedDeclined;
	private float deliveryCharge;
	
	
	
	public float getDeliveryCharge() {
		return deliveryCharge;
	}
	public void setDeliveryCharge(float deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}
	public String getCustomerBuilderAceptedDeclined() {
		return customerBuilderAceptedDeclined;
	}
	public void setCustomerBuilderAceptedDeclined(String customerBuilderAceptedDeclined) {
		this.customerBuilderAceptedDeclined = customerBuilderAceptedDeclined;
	}
	public int getMaterialRequirementItemId() {
		return materialRequirementItemId;
	}
	public void setMaterialRequirementItemId(int materialRequirementItemId) {
		this.materialRequirementItemId = materialRequirementItemId;
	}
	public int getMaterialRequirementId() {
		return materialRequirementId;
	}
	public void setMaterialRequirementId(int materialRequirementId) {
		this.materialRequirementId = materialRequirementId;
	}
	public int getMaterialSupplierId() {
		return materialSupplierId;
	}
	public void setMaterialSupplierId(int materialSupplierId) {
		this.materialSupplierId = materialSupplierId;
	}
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
