package com.shop.organic.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

public class SiteLocationDTO {

	private int siteLocationId;
	private int customerRequirementId;
	private String siteLocationFilePath;
	private String videoFilePath;
	private byte[] landImagePNGorJPGFileFormat;
	
	public byte[] getLandImagePNGorJPGFileFormat() {
		return landImagePNGorJPGFileFormat;
	}
	public void setLandImagePNGorJPGFileFormat(byte[] landImagePNGorJPGFileFormat) {
		this.landImagePNGorJPGFileFormat = landImagePNGorJPGFileFormat;
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
