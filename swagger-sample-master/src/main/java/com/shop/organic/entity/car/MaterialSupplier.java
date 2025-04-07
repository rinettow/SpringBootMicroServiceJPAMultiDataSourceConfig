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
@Table(name = "MATERIAL_SUPPLIER")
public class MaterialSupplier {

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
	@Column(name = "MATERIAL_SUPPLIER_ID")
	private int materialSupplierBuilderId;

	@Column(name = "MATERIAL_SUPPLIER_NAME")
	private String materialSupplierBuilderName;

	@Column(name = "MATERIAL_SUPPLIER_COMPANY")
	private String materialSupplierCompany;

	
	@Column(name = "MATERIAL_SUPPLIER_PHONE")
	private String materialSupplierPhone;
	
	@Column(name = "MATERIAL_SUPPLIER_USER_NAME")
	private String materialSupplierUserName;

	@Column(name = "MATERIAL_SUPPLIER_PASSWORD")
	private String materialSupplierPassword;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MATERIAL_SUPPLIER_ADDRESS_ID", referencedColumnName = "MATERIAL_SUPPLIER_ADDRESS_ID")
	private MaterialSupplierAddress materialSupplierAddress;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialSupplierForMaterialRequirementItemsEstimate")
	private List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialSupplierForSupplierAvailableCategories")
	private List<SupplierAvailableCategories> materialSupplierAvailableCategories;
	
	

	public List<SupplierAvailableCategories> getMaterialSupplierAvailableCategories() {
		return materialSupplierAvailableCategories;
	}

	public void setMaterialSupplierAvailableCategories(
			List<SupplierAvailableCategories> materialSupplierAvailableCategories) {
		this.materialSupplierAvailableCategories = materialSupplierAvailableCategories;
	}

	public List<MaterialRequirementItemsEstimate> getMaterialRequirementItemsEstimate() {
		return materialRequirementItemsEstimate;
	}

	public void setMaterialRequirementItemsEstimate(
			List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate) {
		this.materialRequirementItemsEstimate = materialRequirementItemsEstimate;
	}

	public int getMaterialSupplierBuilderId() {
		return materialSupplierBuilderId;
	}

	public void setMaterialSupplierBuilderId(int materialSupplierBuilderId) {
		this.materialSupplierBuilderId = materialSupplierBuilderId;
	}

	public String getMaterialSupplierBuilderName() {
		return materialSupplierBuilderName;
	}

	public void setMaterialSupplierBuilderName(String materialSupplierBuilderName) {
		this.materialSupplierBuilderName = materialSupplierBuilderName;
	}

	public String getMaterialSupplierCompany() {
		return materialSupplierCompany;
	}

	public void setMaterialSupplierCompany(String materialSupplierCompany) {
		this.materialSupplierCompany = materialSupplierCompany;
	}

	public String getMaterialSupplierPhone() {
		return materialSupplierPhone;
	}

	public void setMaterialSupplierPhone(String materialSupplierPhone) {
		this.materialSupplierPhone = materialSupplierPhone;
	}

	public String getMaterialSupplierUserName() {
		return materialSupplierUserName;
	}

	public void setMaterialSupplierUserName(String materialSupplierUserName) {
		this.materialSupplierUserName = materialSupplierUserName;
	}

	public String getMaterialSupplierPassword() {
		return materialSupplierPassword;
	}

	public void setMaterialSupplierPassword(String materialSupplierPassword) {
		this.materialSupplierPassword = materialSupplierPassword;
	}

	public MaterialSupplierAddress getMaterialSupplierAddress() {
		return materialSupplierAddress;
	}

	public void setMaterialSupplierAddress(MaterialSupplierAddress materialSupplierAddress) {
		this.materialSupplierAddress = materialSupplierAddress;
	}

	
	

	

}
