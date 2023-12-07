package com.shop.organic.dto;

import javax.persistence.Column;

public class EntertainmentOptionDTO {

    private String entertainmentOptionId;
    private boolean USB;	
	private boolean cdPlayer;	
	private boolean AUX;	
	private boolean bluetooth;
	
	public String getEntertainmentOptionId() {
		return entertainmentOptionId;
	}
	public void setEntertainmentOptionId(String entertainmentOptionId) {
		this.entertainmentOptionId = entertainmentOptionId;
	}
	public boolean isUSB() {
		return USB;
	}
	public void setUSB(boolean uSB) {
		USB = uSB;
	}
	public boolean isCdPlayer() {
		return cdPlayer;
	}
	public void setCdPlayer(boolean cdPlayer) {
		this.cdPlayer = cdPlayer;
	}
	public boolean isAUX() {
		return AUX;
	}
	public void setAUX(boolean aUX) {
		AUX = aUX;
	}
	public boolean isBluetooth() {
		return bluetooth;
	}
	public void setBluetooth(boolean bluetooth) {
		this.bluetooth = bluetooth;
	}
	
}
