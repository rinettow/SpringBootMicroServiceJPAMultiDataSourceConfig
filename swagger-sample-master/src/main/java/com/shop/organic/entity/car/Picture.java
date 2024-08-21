package com.shop.organic.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PICTURE")
public class Picture {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PICTURE_ID", insertable = false, updatable = false)
	private int pictureId;
	
	@Column(name = "PROJECT_ID")
	private int projectId;
	

	@Column(name = "PICTURE_FILE_PATH")
	private String pictureFilePath;
	
	
	@Column(name = "VIDEO_FILE_PATH")
	private String videoFilePath;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID", insertable = false, updatable = false)
	private Projects projectForPicture;
	
	@Column(name = "ROOM_TYPE")
	private String roomType;
	
	@Column(name = "ROOM_DESCRIPTION")
	private String roomDescription;
	
	
	//@Column(name = "ROOM_SQUARE_FEET")
	//private int roomSquareFeet;
	
	@Column(name = "MATERIAL_BRAND")
	private String materialBrand;
	
	@Column(name = "PAINT_BRAND")
	private String paintBrand;
	
	@Column(name = "PLUMBING_BRAND")
	private String plumbingBrand;
	
	@Column(name = "ELECTRICAL_BRAND")
	private String electricalBrand;
	
	@Column(name = "CEMENT_BRAND")
	private String cementBrand;
	
	@Column(name = "STEEL_BRAND")
	private String steelBrand;
	
	
	public String getPaintBrand() {
		return paintBrand;
	}
	public void setPaintBrand(String paintBrand) {
		this.paintBrand = paintBrand;
	}
	public String getPlumbingBrand() {
		return plumbingBrand;
	}
	public void setPlumbingBrand(String plumbingBrand) {
		this.plumbingBrand = plumbingBrand;
	}
	public String getElectricalBrand() {
		return electricalBrand;
	}
	public void setElectricalBrand(String electricalBrand) {
		this.electricalBrand = electricalBrand;
	}
	public String getCementBrand() {
		return cementBrand;
	}
	public void setCementBrand(String cementBrand) {
		this.cementBrand = cementBrand;
	}
	public String getSteelBrand() {
		return steelBrand;
	}
	public void setSteelBrand(String steelBrand) {
		this.steelBrand = steelBrand;
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
	
	
	public Projects getProjectForPicture() {
		return projectForPicture;
	}
	public void setProjectForPicture(Projects projectForPicture) {
		this.projectForPicture = projectForPicture;
	}
	public String getVideoFilePath() {
		return videoFilePath;
	}
	public void setVideoFilePath(String videoFilePath) {
		this.videoFilePath = videoFilePath;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public int getPictureId() {
		return pictureId;
	}
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}

	public String getPictureFilePath() {
		return pictureFilePath;
	}
	public void setPictureFilePath(String pictureFilePath) {
		this.pictureFilePath = pictureFilePath;
	}
	
}
