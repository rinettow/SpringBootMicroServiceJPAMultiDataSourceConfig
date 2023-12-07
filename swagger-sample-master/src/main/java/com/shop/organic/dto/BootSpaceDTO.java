package com.shop.organic.dto;

import javax.persistence.Column;

public class BootSpaceDTO {
	
    private String bootSpaceId;
    private int bigSuitcase;
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
