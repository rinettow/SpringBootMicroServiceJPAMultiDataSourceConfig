package com.shop.organic.entity.car;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "MATERIAL_REQUIREMENT_ITEMS")
public class MaterialRequirementItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*
	 * @Id
	 * 
	 * @GeneratedValue(generator = "sequence-generator")
	 * 
	 * @GenericGenerator( name = "sequence-generator", strategy =
	 * "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
	 * 
	 * @Parameter(name = "sequence_name", value = "Builder_sequence"),
	 * 
	 * @Parameter(name = "initial_value", value = "1"),
	 * 
	 * @Parameter(name = "increment_size", value = "1") } )
	 */
	@Column(name = "MATERIAL_REQUIREMENT_ITEMS_ID")
	private int materialRequirementItemsId;
	
	@Column(name = "MATERIAL_REQUIREMENT_ID")
	private int materialRequirementId;
	
	@Column(name = "PRODUCT_ID")
	private int productId;
	
	@Column(name = "PRODUCT_SUB_CATEGORY_ID")
	private int productSubcategoryId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_REQUIREMENT_ID", insertable = false, updatable = false)
	private MaterialRequirement materialRequirementForMaterialRequirementItems;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", insertable = false, updatable = false)
	private Product productForMaterialRequirementItems;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_SUB_CATEGORY_ID", insertable = false, updatable = false)
	private ProductSubCategory productSubCategoryForMaterialRequirementItems;
	

	@Column(name = "QUANTITY")
	private int quantity;


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialRequirementItemsForMaterialRequirementItemsEstimate")
	private List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate;


	
	
	public int getMaterialRequirementId() {
		return materialRequirementId;
	}


	public void setMaterialRequirementId(int materialRequirementId) {
		this.materialRequirementId = materialRequirementId;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	public int getProductSubcategoryId() {
		return productSubcategoryId;
	}


	public void setProductSubcategoryId(int productSubcategoryId) {
		this.productSubcategoryId = productSubcategoryId;
	}


	public List<MaterialRequirementItemsEstimate> getMaterialRequirementItemsEstimate() {
		return materialRequirementItemsEstimate;
	}


	public void setMaterialRequirementItemsEstimate(
			List<MaterialRequirementItemsEstimate> materialRequirementItemsEstimate) {
		this.materialRequirementItemsEstimate = materialRequirementItemsEstimate;
	}


	public int getMaterialRequirementItemsId() {
		return materialRequirementItemsId;
	}


	public void setMaterialRequirementItemsId(int materialRequirementItemsId) {
		this.materialRequirementItemsId = materialRequirementItemsId;
	}


	public MaterialRequirement getMaterialRequirementForMaterialRequirementItems() {
		return materialRequirementForMaterialRequirementItems;
	}


	public void setMaterialRequirementForMaterialRequirementItems(
			MaterialRequirement materialRequirementForMaterialRequirementItems) {
		this.materialRequirementForMaterialRequirementItems = materialRequirementForMaterialRequirementItems;
	}


	public Product getProductForMaterialRequirementItems() {
		return productForMaterialRequirementItems;
	}


	public void setProductForMaterialRequirementItems(Product productForMaterialRequirementItems) {
		this.productForMaterialRequirementItems = productForMaterialRequirementItems;
	}


	public ProductSubCategory getProductSubCategoryForMaterialRequirementItems() {
		return productSubCategoryForMaterialRequirementItems;
	}


	public void setProductSubCategoryForMaterialRequirementItems(
			ProductSubCategory productSubCategoryForMaterialRequirementItems) {
		this.productSubCategoryForMaterialRequirementItems = productSubCategoryForMaterialRequirementItems;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	
		

}
