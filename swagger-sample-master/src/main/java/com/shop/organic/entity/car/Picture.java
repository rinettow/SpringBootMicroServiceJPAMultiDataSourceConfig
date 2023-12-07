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
	private String pictureId;
	
	@Column(name = "CAR_ID")
	private String carId;
	
	@ManyToOne
	@JoinColumn(name="CAR_ID", insertable = false, updatable = false)
	private Car car;
	
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	@Column(name = "PICTURE_FILE_PATH")
	private String pictureFilePath;
	
	public String getPictureId() {
		return pictureId;
	}
	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}
	/*public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}*/
	public String getPictureFilePath() {
		return pictureFilePath;
	}
	public void setPictureFilePath(String pictureFilePath) {
		this.pictureFilePath = pictureFilePath;
	}
	
}
