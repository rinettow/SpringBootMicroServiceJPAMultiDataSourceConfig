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
@Table(name = "BUILDERS_OTP")
public class BuilderOtp {

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
	@Column(name = "BUILDERS_OTP_ID")
	private int builderOtpId;

	@Column(name = "BUILDERS_PHONE_NUMBER")
	private String builderPhoneNumber;

	@Column(name = "BUILDERS_OTP_NUMBER")
	private int buildersOtpNumber;
	
	public int getBuilderOtpId() {
		return builderOtpId;
	}

	public void setBuilderOtpId(int builderOtpId) {
		this.builderOtpId = builderOtpId;
	}

	public String getBuilderPhoneNumber() {
		return builderPhoneNumber;
	}

	public void setBuilderPhoneNumber(String builderPhoneNumber) {
		this.builderPhoneNumber = builderPhoneNumber;
	}

	public int getBuildersOtpNumber() {
		return buildersOtpNumber;
	}

	public void setBuildersOtpNumber(int buildersOtpNumber) {
		this.buildersOtpNumber = buildersOtpNumber;
	}

	
}
