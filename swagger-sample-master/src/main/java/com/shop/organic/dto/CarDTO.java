package com.shop.organic.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.BootSpace;
import com.shop.organic.entity.car.EntertainmentOption;
import com.shop.organic.entity.car.Mileage;
import com.shop.organic.entity.car.Picture;

public class CarDTO {

	private String carId;
	private String carName;
	private String manufacturingCompany;
	private String make;
	private int modelYear;
	private String variant;
	private String manualOrAuto;
	private String fuelType;
	private int seatCount;
	private float costPerKilometer;
	private float costPerHour;	
	private AddressDTO address;
	private BootSpaceDTO bootSpace;
	private boolean navigationInBuild;
	private EntertainmentOptionDTO entertainmentOption;
	private MileageDTO mileage;
	public MileageDTO getMileage() {
		return mileage;
	}
	public void setMileage(MileageDTO mileage) {
		this.mileage = mileage;
	}
	private List<PictureDTO> PicturesDTO;
	private float totalMileage;	
	private String color;
	private boolean sunRoof;
	private boolean luxary;
	private boolean cruseControl;
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
	public AddressDTO getAddress() {
		return address;
	}
	public void setAddress(AddressDTO address) {
		this.address = address;
	}
	public BootSpaceDTO getBootSpace() {
		return bootSpace;
	}
	public void setBootSpace(BootSpaceDTO bootSpace) {
		this.bootSpace = bootSpace;
	}
	public boolean isNavigationInBuild() {
		return navigationInBuild;
	}
	public void setNavigationInBuild(boolean navigationInBuild) {
		this.navigationInBuild = navigationInBuild;
	}
	public EntertainmentOptionDTO getEntertainmentOption() {
		return entertainmentOption;
	}
	public void setEntertainmentOption(EntertainmentOptionDTO entertainmentOption) {
		this.entertainmentOption = entertainmentOption;
	}
	
	public List<PictureDTO> getPicturesDTO() {
		return PicturesDTO;
	}
	public void setPicturesDTO(List<PictureDTO> picturesDTO) {
		PicturesDTO = picturesDTO;
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
