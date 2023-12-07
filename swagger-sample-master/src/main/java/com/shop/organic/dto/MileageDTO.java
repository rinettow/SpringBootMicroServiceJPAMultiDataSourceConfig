package com.shop.organic.dto;

import javax.persistence.Column;

public class MileageDTO {

	private String mileageId;
	private float cityMileage;
	private float highwayMileage;
	
	public String getMileageId() {
		return mileageId;
	}
	public void setMileageId(String mileageId) {
		this.mileageId = mileageId;
	}
	public float getCityMileage() {
		return cityMileage;
	}
	public void setCityMileage(float cityMileage) {
		this.cityMileage = cityMileage;
	}
	public float getHighwayMileage() {
		return highwayMileage;
	}
	public void setHighwayMileage(float highwayMileage) {
		this.highwayMileage = highwayMileage;
	}
	
}
