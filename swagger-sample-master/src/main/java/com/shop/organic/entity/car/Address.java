package com.shop.organic.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "ADDRESS")
public class Address {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	/*@Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "Address_sequence"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "1")
        }
    )*/
	@Column(name = "ADDRESS_ID", insertable = false, updatable = false)
	private int addressId;
	
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
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "DISTRICT")
	private String district;
	
	@Column(name = "PINCODE")
	private String pincode;
	
	@Column(name = "COUNTRY")
	private String country;
	
	

	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
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
