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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CAR")
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CAR_ID")
	private String carId;
	
	@Column(name = "CAR_NAME")
	private String carName;
	
	@Column(name = "MANUFACTURING_COMPANY")
	private String manufacturingCompany;
	
	@Column(name = "MAKE")
	private String make;
	
	@Column(name = "MODEL_YEAR")
	private int modelYear;
	
	@Column(name = "VARIANT")
	private String variant;
	
	@Column(name = "MANUAL_AUTOMATIC")
	private String manualOrAuto;
	
	@Column(name = "FUEL_TYPE")
	private String fuelType;
	
	@Column(name = "SEAT_COUNT")
	private int seatCount;
	
	@Column(name = "COST_PER_KILOMETER")
	private float costPerKilometer;
	
	@Column(name = "COST_PER_HOUR")
	private float costPerHour;	
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
	private Address address;
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOT_SPACE_ID", referencedColumnName = "BOOT_SPACE_ID")
	private BootSpace bootSpace;
	
	public BootSpace getBootSpace() {
		return bootSpace;
	}
	public void setBootSpace(BootSpace bootSpace) {
		this.bootSpace = bootSpace;
	}

	@Column(name = "NAVIGATION_IN_BUILD")
	private boolean navigationInBuild;
	
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ENTERTAINMENT_OPTION_ID", referencedColumnName = "ENTERTAINMENT_OPTION_ID")
	private EntertainmentOption entertainmentOption;
	
	public EntertainmentOption getEntertainmentOption() {
		return entertainmentOption;
	}
	public void setEntertainmentOption(EntertainmentOption entertainmentOption) {
		this.entertainmentOption = entertainmentOption;
	}

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MILEAGE_ID", referencedColumnName = "MILEAGE_ID")
	private Mileage mileage;
	
	public Mileage getMileage() {
		return mileage;
	}
	public void setMileage(Mileage mileage) {
		this.mileage = mileage;
	}
	
	@OneToMany(mappedBy="car", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Picture> Pictures;

	public List<Picture> getPictures() {
		return Pictures;
	}
	public void setPictures(List<Picture> pictures) {
		Pictures = pictures;
	}

	@Column(name = "TOTAL_MILEAGE")
	private float totalMileage;	
	
	@Column(name = "COLOR")
	private String color;
	
	@Column(name = "SUN_ROOF")
	private boolean sunRoof;
	
	@Column(name = "LUXARY")
	private boolean luxary;
	
	@Column(name = "CRUSE_CONTROL")
	private boolean cruseControl;
	
	@Column(name = "STEERING_CONTROL")
	private boolean steeringControl;
	
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public String getManufacturingCompany() {
		return manufacturingCompany;
	}
	public void setManufacturingCompany(String manufacturingCompany) {
		this.manufacturingCompany = manufacturingCompany;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public int getModelYear() {
		return modelYear;
	}
	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public String getManualOrAuto() {
		return manualOrAuto;
	}
	public void setManualOrAuto(String manualOrAuto) {
		this.manualOrAuto = manualOrAuto;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public int getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	public float getCostPerKilometer() {
		return costPerKilometer;
	}
	public void setCostPerKilometer(float costPerKilometer) {
		this.costPerKilometer = costPerKilometer;
	}
	public float getCostPerHour() {
		return costPerHour;
	}
	public void setCostPerHour(float costPerHour) {
		this.costPerHour = costPerHour;
	}
	
	
	public boolean isNavigationInBuild() {
		return navigationInBuild;
	}
	public void setNavigationInBuild(boolean navigationInBuild) {
		this.navigationInBuild = navigationInBuild;
	}
	
	
	public float getTotalMileage() {
		return totalMileage;
	}
	public void setTotalMileage(float totalMileage) {
		this.totalMileage = totalMileage;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isSunRoof() {
		return sunRoof;
	}
	public void setSunRoof(boolean sunRoof) {
		this.sunRoof = sunRoof;
	}
	public boolean isLuxary() {
		return luxary;
	}
	public void setLuxary(boolean luxary) {
		this.luxary = luxary;
	}
	public boolean isCruseControl() {
		return cruseControl;
	}
	public void setCruseControl(boolean cruseControl) {
		this.cruseControl = cruseControl;
	}
	public boolean isSteeringControl() {
		return steeringControl;
	}
	public void setSteeringControl(boolean steeringControl) {
		this.steeringControl = steeringControl;
	}
	
}
