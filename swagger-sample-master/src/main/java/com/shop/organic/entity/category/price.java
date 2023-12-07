package com.shop.organic.entity.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "Price")
public class price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "price_Id", insertable = false, updatable = false)
	private String priceId;
	
	@Column(name = "CHF")
	private float CHF;
	
	@Column(name = "EURO")
	private float EURO;
	
	@Column(name = "DOLLAR")
	private float DOLLAR;
	
	/*@OneToOne(mappedBy="price")
	@Cascade(value=org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private category category;
	

	public category getCategory() {
		return category;
	}

	public void setCategory(category category) {
		this.category = category;
	}*/

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public float getCHF() {
		return CHF;
	}

	public void setCHF(float cHF) {
		CHF = cHF;
	}

	public float getEURO() {
		return EURO;
	}

	public void setEURO(float eURO) {
		EURO = eURO;
	}

	public float getDOLLAR() {
		return DOLLAR;
	}

	public void setDOLLAR(float dOLLAR) {
		DOLLAR = dOLLAR;
	}


}
