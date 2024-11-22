package com.shop.organic.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SITE_LOCATION")
public class SiteLocation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SITE_LOCATION_ID", insertable = false, updatable = false)
	private int siteLocationId;

	@Column(name = "CUSTOMER_REQUIREMENT_ID")
	private int customerRequirementId;

	@Column(name = "SITE_LOCATION_FILE_PATH")
	private String siteLocationFilePath;

	@Column(name = "VIDEO_FILE_PATH")
	private String videoFilePath;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_REQUIREMENT_ID", insertable = false, updatable = false)
	private CustomerRequirement customerRequirementForSiteLocation;

	public CustomerRequirement getCustomerRequirementForSiteLocation() {
		return customerRequirementForSiteLocation;
	}

	public void setCustomerRequirementForSiteLocation(CustomerRequirement customerRequirementForSiteLocation) {
		this.customerRequirementForSiteLocation = customerRequirementForSiteLocation;
	}

	public int getSiteLocationId() {
		return siteLocationId;
	}

	public void setSiteLocationId(int siteLocationId) {
		this.siteLocationId = siteLocationId;
	}

	public int getCustomerRequirementId() {
		return customerRequirementId;
	}

	public void setCustomerRequirementId(int customerRequirementId) {
		this.customerRequirementId = customerRequirementId;
	}

	public String getSiteLocationFilePath() {
		return siteLocationFilePath;
	}

	public void setSiteLocationFilePath(String siteLocationFilePath) {
		this.siteLocationFilePath = siteLocationFilePath;
	}

	public String getVideoFilePath() {
		return videoFilePath;
	}

	public void setVideoFilePath(String videoFilePath) {
		this.videoFilePath = videoFilePath;
	}

	
}
