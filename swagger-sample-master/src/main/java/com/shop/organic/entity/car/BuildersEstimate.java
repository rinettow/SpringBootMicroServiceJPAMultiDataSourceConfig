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
@Table(name = "BUILDERS_ESTIMATE")
public class BuildersEstimate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUILDERS_ESTIMATE_ID")
	private int buildersEstimateId;
	
	@Column(name = "CUSTOMER_REQUIREMENT_ID")
	private int customerRequirementId;
	
	//@Column(name = "PROJECT_ID")
	//private int projectId;
	
	@Column(name = "BUILDER_ID")
	private int builderId;

	@Column(name = "PER_SQUAREFEET_COST")
	private float perSquareFeetCost;

	@Column(name = "DETAILED_ESTIMATE_FILEPATH")
	private String detailedEstimateFilePath;
	
	@Column(name = "CUSTOMER_ACCEPTED_DECLINED")
	private String customerAcceptedDeclined;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_REQUIREMENT_ID", insertable = false, updatable = false)
	private CustomerRequirement customerRequirementForBuildersEstimate;
	
	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "PROJECT_ID", insertable = false, updatable = false)
	//private Projects projectForBuildersEstimate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDER_ID", insertable = false, updatable = false)
	private Builder builderForBuildersEstimate;

	public int getBuildersEstimateId() {
		return buildersEstimateId;
	}

	public void setBuildersEstimateId(int buildersEstimateId) {
		this.buildersEstimateId = buildersEstimateId;
	}

	public String getCustomerAcceptedDeclined() {
		return customerAcceptedDeclined;
	}

	public void setCustomerAcceptedDeclined(String customerAcceptedDeclined) {
		this.customerAcceptedDeclined = customerAcceptedDeclined;
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

	public float getPerSquareFeetCost() {
		return perSquareFeetCost;
	}

	public void setPerSquareFeetCost(float perSquareFeetCost) {
		this.perSquareFeetCost = perSquareFeetCost;
	}

	public String getDetailedEstimateFilePath() {
		return detailedEstimateFilePath;
	}

	public void setDetailedEstimateFilePath(String detailedEstimateFilePath) {
		this.detailedEstimateFilePath = detailedEstimateFilePath;
	}

	public CustomerRequirement getCustomerRequirementForBuildersEstimate() {
		return customerRequirementForBuildersEstimate;
	}

	public void setCustomerRequirementForBuildersEstimate(CustomerRequirement customerRequirementForBuildersEstimate) {
		this.customerRequirementForBuildersEstimate = customerRequirementForBuildersEstimate;
	}

	public Builder getBuilderForBuildersEstimate() {
		return builderForBuildersEstimate;
	}

	public void setBuilderForBuildersEstimate(Builder builderForBuildersEstimate) {
		this.builderForBuildersEstimate = builderForBuildersEstimate;
	}

	
}
