package com.shop.organic.dto;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.shop.organic.entity.car.AmenitiesAndSpecifications;
import com.shop.organic.entity.car.Builder;

//import com.shop.organic.entity.car.Car;

public class PictureDTO {
	
    private int pictureId;
    private String pictureFilePath;
	private int projectId;
	private String videoFilePath;
	private byte[] picture;
	private int builderId;
	private String roomType;
	private String roomDescription;
    private int roomSquareFeet;
	private String materialBrand;
	
	
	
	public int getRoomSquareFeet() {
		return roomSquareFeet;
	}
	public void setRoomSquareFeet(int roomSquareFeet) {
		this.roomSquareFeet = roomSquareFeet;
	}
	public String getMaterialBrand() {
		return materialBrand;
	}
	public void setMaterialBrand(String materialBrand) {
		this.materialBrand = materialBrand;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getRoomDescription() {
		return roomDescription;
	}
	public void setRoomDescription(String roomDescription) {
		this.roomDescription = roomDescription;
	}
	
	public int getBuilderId() {
		return builderId;
	}
	public void setBuilderId(int builderId) {
		this.builderId = builderId;
	}
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getVideoFilePath() {
		return videoFilePath;
	}
	public void setVideoFilePath(String videoFilePath) {
		this.videoFilePath = videoFilePath;
	}
	
	public int getPictureId() {
		return pictureId;
	}
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}
	/*public BuilderDTO getBuilder() {
		return builder;
	}
	public void setBuilder(BuilderDTO builder) {
		this.builder = builder;
	}*/
	public String getPictureFilePath() {
		return pictureFilePath;
	}
	public void setPictureFilePath(String pictureFilePath) {
		this.pictureFilePath = pictureFilePath;
	}
	
}
