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

public class BuilderOtpDTO {

	private int builderOtpId;
	private String builderPhoneNumber;
	private int buildersOtpNumber;
	
	public int getBuilderOtpId() {
		return builderOtpId;
	}
	public void setBuilderOtpId(int builderOtpId) {
		this.builderOtpId = builderOtpId;
	}
	public String getBuilderPhoneNumber() {
		return builderPhoneNumber;
	}
	public void setBuilderPhoneNumber(String builderPhoneNumber) {
		this.builderPhoneNumber = builderPhoneNumber;
	}
	public int getBuildersOtpNumber() {
		return buildersOtpNumber;
	}
	public void setBuildersOtpNumber(int buildersOtpNumber) {
		this.buildersOtpNumber = buildersOtpNumber;
	}
	
	
	
}
