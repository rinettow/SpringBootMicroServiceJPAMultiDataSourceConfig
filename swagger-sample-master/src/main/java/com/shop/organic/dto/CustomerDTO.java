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
import com.shop.organic.entity.car.CustomerRequirement;
import com.shop.organic.entity.car.Picture;
import com.shop.organic.entity.car.Projects;

public class CustomerDTO {

	private int customerId;
	private String customerName;
	private String phoneCustomer;
	private List<CustomerRequirementDTO> customerRequirement;


	public List<CustomerRequirementDTO> getCustomerRequirement() {
		return customerRequirement;
	}

	public void setCustomerRequirement(List<CustomerRequirementDTO> customerRequirement) {
		this.customerRequirement = customerRequirement;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneCustomer() {
		return phoneCustomer;
	}

	public void setPhoneCustomer(String phoneCustomer) {
		this.phoneCustomer = phoneCustomer;
	}

}
