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
@Table(name = "SUPPLIERS_OTP")
public class SupplierOtp {

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
	@Column(name = "SUPPLIERS_OTP_ID")
	private int suplierOtpId;

	@Column(name = "SUPPLIERS_PHONE_NUMBER")
	private String suplierPhoneNumber;

	@Column(name = "SUPPLIERS_OTP_NUMBER")
	private int suplierOtpNumber;

	public int getSuplierOtpId() {
		return suplierOtpId;
	}

	public void setSuplierOtpId(int suplierOtpId) {
		this.suplierOtpId = suplierOtpId;
	}

	public String getSuplierPhoneNumber() {
		return suplierPhoneNumber;
	}

	public void setSuplierPhoneNumber(String suplierPhoneNumber) {
		this.suplierPhoneNumber = suplierPhoneNumber;
	}

	public int getSuplierOtpNumber() {
		return suplierOtpNumber;
	}

	public void setSuplierOtpNumber(int suplierOtpNumber) {
		this.suplierOtpNumber = suplierOtpNumber;
	}
	
	
	
}
