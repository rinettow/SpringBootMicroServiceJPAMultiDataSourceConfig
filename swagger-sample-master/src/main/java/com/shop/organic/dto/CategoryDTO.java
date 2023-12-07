package com.shop.organic.dto;

import java.util.List;

import com.shop.organic.entity.category.price;
import com.shop.organic.entity.category.product;

public class CategoryDTO {
	
	private int rowId;
	private String categoryId;	
	private String categoryName;
	private String categoryDesc;
	private String priceId;
	private List<productDTO> products;
	private priceDTO price;
	
	public priceDTO getPrice() {
		return price;
	}
	public void setPrice(priceDTO price) {
		this.price = price;
	}
	public List<productDTO> getProducts() {
		return products;
	}
	public void setProducts(List<productDTO> products) {
		this.products = products;
	}
	
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

}
