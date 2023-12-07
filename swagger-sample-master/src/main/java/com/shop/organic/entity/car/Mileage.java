package com.shop.organic.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MILEAGE")
public class Mileage {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MILEAGE_ID", insertable = false, updatable = false)
	private String mileageId;
	
	@Column(name = "CITY_MILEAGE")
	private float cityMileage;
	
	@Column(name = "HIGHWAY_MILEAGE")
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
