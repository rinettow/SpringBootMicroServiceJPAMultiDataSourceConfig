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

public class MaterialSupplierDTO {

	private int materialSupplierBuilderId;
	private String materialSupplierBuilderName;
	private String materialSupplierCompany;
	private String materialSupplierPhone;
	private String materialSupplierUserName;
	private String materialSupplierPassword;
	private MaterialSupplierAddressDTO materialSupplierAddress;
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
	public MaterialSupplierAddressDTO getMaterialSupplierAddress() {
		return materialSupplierAddress;
	}
	public void setMaterialSupplierAddress(MaterialSupplierAddressDTO materialSupplierAddress) {
		this.materialSupplierAddress = materialSupplierAddress;
	}

	
}
