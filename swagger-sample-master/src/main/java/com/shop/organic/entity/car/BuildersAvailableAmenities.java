package com.shop.organic.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.shop.organic.entity.car.Builder;


@Entity
@Table(name = "BUILDERS_AVAILABLE_AMENITIES")
public class BuildersAvailableAmenities {
	
	@Id
	@GeneratedValue
	@Column(name = "BUILDER_ID", unique = false, nullable = false)
	private int builderId;
	
	@Column(name = "AMENITIES_AND_SPECIFICATIONS_ID")
	private int amenitiesAndSpecificationsId;
	
	@ManyToOne
	@JoinColumn(name="BUILDER_ID", insertable = false, updatable = false)
	private Builder builder;
	
	
	@ManyToOne
	@JoinColumn(name="AMENITIES_AND_SPECIFICATIONS_ID", insertable = false, updatable = false)
	private AmenitiesAndSpecifications amenitiesAndSpecifications;
	
	
	public Builder getBuilder() {
		return builder;
	}
	public void setBuilder(Builder builder) {
		this.builder = builder;
	}
	public AmenitiesAndSpecifications getAmenitiesAndSpecifications() {
		return amenitiesAndSpecifications;
	}
	public void setAmenitiesAndSpecifications(AmenitiesAndSpecifications amenitiesAndSpecifications) {
		this.amenitiesAndSpecifications = amenitiesAndSpecifications;
	}
	
	public int getBuilderId() {
		return builderId;
	}
	public void setBuilderId(int builderId) {
		this.builderId = builderId;
	}
	public int getAmenitiesAndSpecificationsId() {
		return amenitiesAndSpecificationsId;
	}
	public void setAmenitiesAndSpecificationsId(int amenitiesAndSpecificationsId) {
		this.amenitiesAndSpecificationsId = amenitiesAndSpecificationsId;
	}
	
}
