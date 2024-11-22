package com.shop.organic.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

public class MaterialSupplierAddressDTO {

	private int materialSupplierAddressId;
	private String doorNumber;
	private String streetFirst;
	private String streetSecond;
	private String landmark;
	private String city;
	private String state;
	private String district;
	private String pincode;
	private String country;

	public int getMaterialSupplierAddressId() {
		return materialSupplierAddressId;
	}

	public void setMaterialSupplierAddressId(int materialSupplierAddressId) {
		this.materialSupplierAddressId = materialSupplierAddressId;
	}

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

}
