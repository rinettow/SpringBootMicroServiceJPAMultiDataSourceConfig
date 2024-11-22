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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "CUSTOMER_RED_QUOTATIONS")
public class CustomerRedQuotations {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMER_RED_QUOTATIONS_ID")
	private int customerRedQuotationsId;
	
	@Column(name = "CUSTOMER_ID")
	private int customerId;
	
	//@Column(name = "PROJECT_ID")
	//private int projectId;
	
	@Column(name = "BUILDERS_ESTIMATE_ID")
	private int builderEstimateIdId;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID", insertable = false, updatable = false)
	private Customer customerForCustomerRedQuotationsId;
	

	public int getCustomerRedQuotationsId() {
		return customerRedQuotationsId;
	}

	public void setCustomerRedQuotationsId(int customerRedQuotationsId) {
		this.customerRedQuotationsId = customerRedQuotationsId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getBuilderEstimateIdId() {
		return builderEstimateIdId;
	}

	public void setBuilderEstimateIdId(int builderEstimateIdId) {
		this.builderEstimateIdId = builderEstimateIdId;
	}

	public Customer getCustomerForCustomerRedQuotationsId() {
		return customerForCustomerRedQuotationsId;
	}

	public void setCustomerForCustomerRedQuotationsId(Customer customerForCustomerRedQuotationsId) {
		this.customerForCustomerRedQuotationsId = customerForCustomerRedQuotationsId;
	}
	
}
