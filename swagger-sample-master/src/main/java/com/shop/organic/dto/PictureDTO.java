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
