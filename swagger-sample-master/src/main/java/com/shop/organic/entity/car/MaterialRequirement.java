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
@Table(name = "MATERIAL_REQUIREMENT")
public class MaterialRequirement {

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
	@Column(name = "MATERIAL_REQUIREMENT_ID")
	private int materialRequirementId;
	
	@Column(name = "CUSTOMER_ID", nullable = true)
	private Integer customerId= null;
	
	@Column(name = "BUILDER_ID", nullable = true)
	private Integer builderId= null;
	
	@Column(name = "PRODUCT_CATEGORY_ID")
	private int productCategoryId;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID", insertable = false, updatable = false, nullable = true)
	private Customer customerForMaterialRequirement;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDER_ID", insertable = false, updatable = false, nullable = true)
	private Builder builderForMaterialRequirement;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATEGORY_ID", insertable = false, updatable = false)
	private ProductCategory categoryForMaterialRequirement;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialRequirementForMaterialRequirementItems")
	private List<MaterialRequirementItems> materialRequirementItems;
	

	@Column(name = "REQUIREMENT_STATUS")
	private String requirementStatus;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "DISTRICT")
	private String district;
	
	@Column(name = "DOOR_NUMBER")
	private String doorNumber;
	
	@Column(name = "STREET_FIRST")
	private String streetFirst;
	
	@Column(name = "STREET_SECOND")
	private String streetSecond;
	
	@Column(name = "LANDMARK")
	private String landmark;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "PINCODE")
	private String pincode;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialRequirementForMaterialRequirementItemsEstimate")
	private List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate;

	
	
	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public String getStreetFirst() {
		return streetFirst;
	}

	public void setStreetFirst(String streetFirst) {
		this.streetFirst = streetFirst;
	}

	public String getStreetSecond() {
		return streetSecond;
	}

	public void setStreetSecond(String streetSecond) {
		this.streetSecond = streetSecond;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(int productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	

	public Integer getBuilderId() {
		return builderId;
	}

	public void setBuilderId(Integer builderId) {
		this.builderId = builderId;
	}

	

	public List<MaterialRequirementItemsEstimate> getMaterialRequirementItemsEstimate() {
		return materialRequirementItemsEstimate;
	}

	public void setMaterialRequirementItemsEstimate(
			List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate) {
		this.materialRequirementItemsEstimate = materialRequirementItemsEstimate;
	}

	public List<MaterialRequirementItems> getMaterialRequirementItems() {
		return materialRequirementItems;
	}

	public void setMaterialRequirementItems(List<MaterialRequirementItems> materialRequirementItems) {
		this.materialRequirementItems = materialRequirementItems;
	}

	public int getMaterialRequirementId() {
		return materialRequirementId;
	}

	public void setMaterialRequirementId(int materialRequirementId) {
		this.materialRequirementId = materialRequirementId;
	}

	public Customer getCustomerForMaterialRequirement() {
		return customerForMaterialRequirement;
	}

	public void setCustomerForMaterialRequirement(Customer customerForMaterialRequirement) {
		this.customerForMaterialRequirement = customerForMaterialRequirement;
	}

	public Builder getBuilderForMaterialRequirement() {
		return builderForMaterialRequirement;
	}

	public void setBuilderForMaterialRequirement(Builder builderForMaterialRequirement) {
		this.builderForMaterialRequirement = builderForMaterialRequirement;
	}

	public ProductCategory getCategoryForMaterialRequirement() {
		return categoryForMaterialRequirement;
	}

	public void setCategoryForMaterialRequirement(ProductCategory categoryForMaterialRequirement) {
		this.categoryForMaterialRequirement = categoryForMaterialRequirement;
	}

	public String getRequirementStatus() {
		return requirementStatus;
	}

	public void setRequirementStatus(String requirementStatus) {
		this.requirementStatus = requirementStatus;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

		

}
