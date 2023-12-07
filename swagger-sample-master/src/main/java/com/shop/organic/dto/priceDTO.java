package com.shop.organic.dto;

import javax.persistence.Column;

public class priceDTO {
	
	private String priceId;
	private float CHF;
	private float EURO;
	private float DOLLAR;

    public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public float getCHF() {
		return CHF;
	}
	public void setCHF(float cHF) {
		CHF = cHF;
	}
	public float getEURO() {
		return EURO;
	}
	public void setEURO(float eURO) {
		EURO = eURO;
	}
	public float getDOLLAR() {
		return DOLLAR;
	}
	public void setDOLLAR(float dOLLAR) {
		DOLLAR = dOLLAR;
	}
	
}
