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
@Table(name = "BUILDER_RED_REQUIREMENTS")
public class BuilderRedRequirements {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUILDER_RED_REQUIREMENTS_ID")
	private int builderRedRequirementsId;
	
	@Column(name = "CUSTOMER_REQUIREMENT_ID")
	private int customerRequirementId;
	
	//@Column(name = "PROJECT_ID")
	//private int projectId;
	
	@Column(name = "BUILDER_ID")
	private int builderId;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_REQUIREMENT_ID", insertable = false, updatable = false)
	private CustomerRequirement customerRequirementForBuilderRedRequirements;
	
	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "PROJECT_ID", insertable = false, updatable = false)
	//private Projects projectForBuildersEstimate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDER_ID", insertable = false, updatable = false)
	private Builder builderForBuilderRedRequirements;

	
	public int getBuilderRedRequirementsId() {
		return builderRedRequirementsId;
	}

	public void setBuilderRedRequirementsId(int builderRedRequirementsId) {
		this.builderRedRequirementsId = builderRedRequirementsId;
	}

	public int getCustomerRequirementId() {
		return customerRequirementId;
	}

	public void setCustomerRequirementId(int customerRequirementId) {
		this.customerRequirementId = customerRequirementId;
	}

	public int getBuilderId() {
		return builderId;
	}

	public void setBuilderId(int builderId) {
		this.builderId = builderId;
	}

	public CustomerRequirement getCustomerRequirementForBuilderRedRequirements() {
		return customerRequirementForBuilderRedRequirements;
	}

	public void setCustomerRequirementForBuilderRedRequirements(
			CustomerRequirement customerRequirementForBuilderRedRequirements) {
		this.customerRequirementForBuilderRedRequirements = customerRequirementForBuilderRedRequirements;
	}

	public Builder getBuilderForBuilderRedRequirements() {
		return builderForBuilderRedRequirements;
	}

	public void setBuilderForBuilderRedRequirements(Builder builderForBuilderRedRequirements) {
		this.builderForBuilderRedRequirements = builderForBuilderRedRequirements;
	}

	
	

	
}
