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
@Table(name = "MATERIAL_SUPPLIERS_OTP")
public class MaterialSuppliersOtp {

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
	@Column(name = "MATERIAL_SUPPLIERS_OTP_ID")
	private int materialSuppliersOtpId;

	@Column(name = "MATERIAL_SUPPLIERS_PHONE_NUMBER")
	private String materialSuppliersPhoneNumber;

	@Column(name = "MATERIAL_SUPPLIERS_OTP_NUMBER")
	private int materialSuppliersOtpNumber;

	public int getMaterialSuppliersOtpId() {
		return materialSuppliersOtpId;
	}

	public void setMaterialSuppliersOtpId(int materialSuppliersOtpId) {
		this.materialSuppliersOtpId = materialSuppliersOtpId;
	}

	public String getMaterialSuppliersPhoneNumber() {
		return materialSuppliersPhoneNumber;
	}

	public void setMaterialSuppliersPhoneNumber(String materialSuppliersPhoneNumber) {
		this.materialSuppliersPhoneNumber = materialSuppliersPhoneNumber;
	}

	public int getMaterialSuppliersOtpNumber() {
		return materialSuppliersOtpNumber;
	}

	public void setMaterialSuppliersOtpNumber(int materialSuppliersOtpNumber) {
		this.materialSuppliersOtpNumber = materialSuppliersOtpNumber;
	}
	
	
	
}
