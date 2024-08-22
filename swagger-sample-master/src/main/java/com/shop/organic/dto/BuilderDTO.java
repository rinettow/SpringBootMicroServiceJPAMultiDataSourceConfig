package com.shop.organic.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.shop.organic.entity.car.Address;
import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.BuildersAvailableAmenities;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;

public class BuilderDTO {

	private int builderId;
	private String builderName;
	private String manufacturingCompany;
	private String projectType;
	private AddressDTO address;
	private List<ProjectsDTO> projects;
	private String phone;
	private List<BuildersAvailableAmenitiesDTO> buildersAvailableAmenities;
	private String userName;
	private String password;

	public List<ProjectsDTO> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectsDTO> projects) {
		this.projects = projects;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getBuilderId() {
		return builderId;
	}

	public void setBuilderId(int builderId) {
		this.builderId = builderId;
	}

	public String getBuilderName() {
		return builderName;
	}

	public void setBuilderName(String builderName) {
		this.builderName = builderName;
	}

	public String getManufacturingCompany() {
		return manufacturingCompany;
	}

	public void setManufacturingCompany(String manufacturingCompany) {
		this.manufacturingCompany = manufacturingCompany;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public List<BuildersAvailableAmenitiesDTO> getBuildersAvailableAmenities() {
		return buildersAvailableAmenities;
	}

	public void setBuildersAvailableAmenities(List<BuildersAvailableAmenitiesDTO> buildersAvailableAmenities) {
		this.buildersAvailableAmenities = buildersAvailableAmenities;
	}

}
