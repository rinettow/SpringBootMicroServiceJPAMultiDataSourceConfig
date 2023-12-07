package com.shop.organic.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOT_SPACE")
public class BootSpace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOT_SPACE_ID", insertable = false, updatable = false)
	private String bootSpaceId;
	
	@Column(name = "BIG_SUITCASE")
	private int bigSuitcase;
	
	@Column(name = "SMALL_SUITCASE")
	private int smallSuitcase;
	
	
	public String getBootSpaceId() {
		return bootSpaceId;
	}
	public void setBootSpaceId(String bootSpaceId) {
		this.bootSpaceId = bootSpaceId;
	}
	public int getBigSuitcase() {
		return bigSuitcase;
	}
	public void setBigSuitcase(int bigSuitcase) {
		this.bigSuitcase = bigSuitcase;
	}
	public int getSmallSuitcase() {
		return smallSuitcase;
	}
	public void setSmallSuitcase(int smallSuitcase) {
		this.smallSuitcase = smallSuitcase;
	}
	
}
