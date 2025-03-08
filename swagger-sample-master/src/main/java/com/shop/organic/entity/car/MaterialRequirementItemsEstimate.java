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
@Table(name = "MATERIAL_REQUIREMENT_ITEMS_ESTIMATE")
public class MaterialRequirementItemsEstimate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*
	 * @Id
	 * 
	 * @GeneratedValue(generator = "sequence-generator")
	 * 
	 * @GenericGenerator( name = "sequence-generator", strategy =
	 * "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
	 * 
	 * @Parameter(name = "sequence_name", value = "Builder_sequence"),
	 * 
	 * @Parameter(name = "initial_value", value = "1"),
	 * 
	 * @Parameter(name = "increment_size", value = "1") } )
	 */
	@Column(name = "MATERIAL_REQUIREMENT_ITEMS_ESTIMATE_ID")
	private int materialRequirementItemEstmtimateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_REQUIREMENT_ITEMS_ID", insertable = false, updatable = false)
	private MaterialRequirementItems materialRequirementItemsForMaterialRequirementItemsEstimate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_REQUIREMENT_ID", insertable = false, updatable = false)
	private MaterialRequirement materialRequirementForMaterialRequirementItemsEstimate;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_SUPPLIER_ID", insertable = false, updatable = false)
	private MaterialSupplier materialSupplierForMaterialRequirementItemsEstimate;
	

	@Column(name = "TOTAL_PRICE")
	private float totalPrice;


	public int getMaterialRequirementItemEstmtimateId() {
		return materialRequirementItemEstmtimateId;
	}


	public void setMaterialRequirementItemEstmtimateId(int materialRequirementItemEstmtimateId) {
		this.materialRequirementItemEstmtimateId = materialRequirementItemEstmtimateId;
	}


	public MaterialRequirementItems getMaterialRequirementItemsForMaterialRequirementItemsEstimate() {
		return materialRequirementItemsForMaterialRequirementItemsEstimate;
	}


	public void setMaterialRequirementItemsForMaterialRequirementItemsEstimate(
			MaterialRequirementItems materialRequirementItemsForMaterialRequirementItemsEstimate) {
		this.materialRequirementItemsForMaterialRequirementItemsEstimate = materialRequirementItemsForMaterialRequirementItemsEstimate;
	}


	public MaterialRequirement getMaterialRequirementForMaterialRequirementItemsEstimate() {
		return materialRequirementForMaterialRequirementItemsEstimate;
	}


	public void setMaterialRequirementForMaterialRequirementItemsEstimate(
			MaterialRequirement materialRequirementForMaterialRequirementItemsEstimate) {
		this.materialRequirementForMaterialRequirementItemsEstimate = materialRequirementForMaterialRequirementItemsEstimate;
	}


	public MaterialSupplier getMaterialSupplierForMaterialRequirementItemsEstimate() {
		return materialSupplierForMaterialRequirementItemsEstimate;
	}


	public void setMaterialSupplierForMaterialRequirementItemsEstimate(
			MaterialSupplier materialSupplierForMaterialRequirementItemsEstimate) {
		this.materialSupplierForMaterialRequirementItemsEstimate = materialSupplierForMaterialRequirementItemsEstimate;
	}


	public float getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}


	

	
	
		

}
