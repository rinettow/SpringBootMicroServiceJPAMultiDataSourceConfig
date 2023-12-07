package com.shop.organic.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ENTERTAINMENT_OPTION")
public class EntertainmentOption {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ENTERTAINMENT_OPTION_ID", insertable = false, updatable = false)
	private String entertainmentOptionId;
	
	@Column(name = "USB")
	private boolean USB;	
	
	@Column(name = "CD_PLAYER")
	private boolean cdPlayer;	
	
	@Column(name = "AUX")
	private boolean AUX;	
	
	@Column(name = "Bluetooth")
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
