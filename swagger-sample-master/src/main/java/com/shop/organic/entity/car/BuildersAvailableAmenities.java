package com.shop.organic.entity.car;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.organic.entity.car.Builder;

@Entity
@Table(name = "BUILDERS_AVAILABLE_AMENITIES")
public class BuildersAvailableAmenities {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUILDERS_AVAILABLE_AMENITIES_ID", unique = false, nullable = false)
	private int buildersAvailableAmenitiesId;

	@Column(name = "BUILDER_ID", unique = false, nullable = false)
	private int builderId;

	@Column(name = "AMENITIES_AND_SPECIFICATIONS_ID")
	private int amenitiesAndSpecificationsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDER_ID", insertable = false, updatable = false)
	private Builder builderForAmenity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AMENITIES_AND_SPECIFICATIONS_ID", insertable = false, updatable = false)
	private AmenitiesAndSpecifications amenitiesAndSpecificationsForBuilders;

	public AmenitiesAndSpecifications getAmenitiesAndSpecificationsForBuilders() {
		return amenitiesAndSpecificationsForBuilders;
	}

	public void setAmenitiesAndSpecificationsForBuilders(
			AmenitiesAndSpecifications amenitiesAndSpecificationsForBuilders) {
		this.amenitiesAndSpecificationsForBuilders = amenitiesAndSpecificationsForBuilders;
	}

	public int getBuildersAvailableAmenitiesId() {
		return buildersAvailableAmenitiesId;
	}

	public void setBuildersAvailableAmenitiesId(int buildersAvailableAmenitiesId) {
		this.buildersAvailableAmenitiesId = buildersAvailableAmenitiesId;
	}

	public Builder getBuilderForAmenity() {
		return builderForAmenity;
	}

	public void setBuilderForAmenity(Builder builderForAmenity) {
		this.builderForAmenity = builderForAmenity;
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
