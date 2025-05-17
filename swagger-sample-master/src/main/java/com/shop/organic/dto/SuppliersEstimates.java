package com.shop.organic.dto;

import java.util.List;
import java.util.Map;

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
import com.shop.organic.entity.car.MaterialRequirementItems;

public class SuppliersEstimates {

	private MaterialSupplierDTO materialSupplier;
	private List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate;
	public MaterialSupplierDTO getMaterialSupplier() {
		return materialSupplier;
	}
	public void setMaterialSupplier(MaterialSupplierDTO materialSupplier) {
		this.materialSupplier = materialSupplier;
	}
	public List<MaterialRequirementItemsEstimateDTO> getMaterialRequirementItemsEstimate() {
		return materialRequirementItemsEstimate;
	}
	public void setMaterialRequirementItemsEstimate(
			List<MaterialRequirementItemsEstimateDTO> materialRequirementItemsEstimate) {
		this.materialRequirementItemsEstimate = materialRequirementItemsEstimate;
	}
	
	
}
