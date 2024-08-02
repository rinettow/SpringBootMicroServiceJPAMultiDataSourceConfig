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
	
	
	@ManyToOne
	@JoinColumn(name="PROJECT_ID", insertable = false, updatable = false)
	private Projects projects;
	
	@Column(name = "ROOM_TYPE")
	private String roomType;
	
	@Column(name = "ROOM_DESCRIPTION")
	private String roomDescription;
	
	
	//@Column(name = "ROOM_SQUARE_FEET")
	//private int roomSquareFeet;
	
	@Column(name = "MATERIAL_BRAND")
	private String materialBrand;
	
	
	
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
	
	public Projects getProjects() {
		return projects;
	}
	public void setProjects(Projects projects) {
		this.projects = projects;
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
