package com.shop.organic.entity.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Product")
public class product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_Id", insertable = false, updatable = false)
	private String productId;
	
	@Column(name = "product_Name")
	private String productName;
	
	@Column(name = "product_Desc")
	private String productDesc;
	
	@Column(name = "category_Id")
	private String categoryId;
	
	@Column(name = "quantity")
	private float quantity;
	
	@Column(name = "measurement_Unit")
	private String measurementUnit;
	
	@ManyToOne
	@JoinColumn(name="category_Id", insertable = false, updatable = false)
	private category category;
	
	public category getCategory() {
		return category;
	}

	public void setCategory(category category) {
		this.category = category;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

}
