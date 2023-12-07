package com.shop.organic.entity.category;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Category")
public class category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_Id")
	private String categoryId;
	
	@Column(name = "category_Name")
	private String categoryName;
	
	@Column(name = "category_Desc")
	private String categoryDesc;
	
	@Column(name = "price_Id")
	private String priceId;
	
	@OneToMany(mappedBy="category", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private List<product> products;
	
	public List<product> getProducts() {
		return products;
	}

	public void setProducts(List<product> products) {
		this.products = products;
	}
	
	@OneToOne(cascade=CascadeType.ALL)//one-to-one
    @JoinColumn(name="price_Id", insertable = false, updatable = false)
	private price price;

	public price getPrice() {
		return price;
	}

	public void setPrice(price price) {
		this.price = price;
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

}
