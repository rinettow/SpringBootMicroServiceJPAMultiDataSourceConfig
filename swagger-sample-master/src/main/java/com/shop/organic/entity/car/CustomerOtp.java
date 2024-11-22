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
@Table(name = "CUSTOMERS_OTP")
public class CustomerOtp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*
	 * @Id
	 * 
	 * @GeneratedValue(generator = "sequence-generator")
	 * 
	 * @GenericGenerator( name = "sequence-generator", strategy =
	 * "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
	 * 
	 * @Parameter(name = "sequence_name", value = "Builder_sequence"),
	 * 
	 * @Parameter(name = "initial_value", value = "1"),
	 * 
	 * @Parameter(name = "increment_size", value = "1") } )
	 */
	@Column(name = "CUSTOMERS_OTP_ID")
	private int customerOtpId;

	@Column(name = "CUSTOMERS_PHONE_NUMBER")
	private String customerPhoneNumber;

	@Column(name = "CUSTOMERS_OTP_NUMBER")
	private int customerOtpNumber;

	public int getCustomerOtpId() {
		return customerOtpId;
	}

	public void setCustomerOtpId(int customerOtpId) {
		this.customerOtpId = customerOtpId;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public int getCustomerOtpNumber() {
		return customerOtpNumber;
	}

	public void setCustomerOtpNumber(int customerOtpNumber) {
		this.customerOtpNumber = customerOtpNumber;
	}
	
	

	
}
