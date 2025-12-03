package com.parent.pg.dto;

import java.util.List;	

import com.fasterxml.jackson.annotation.JsonInclude;
import com.parent.pg.model.Address;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PgResponse {

	private Long id;
	private String name;
	private String type;
	private Double price;
	private String rules;
	private Boolean availability;
	private Address address;
	private Long ownerId;
	private String ownerName;
	private String ownerEmail;

	private List<FloorResponse> floors;
	private List<AmenityResponse> amenities;
	private List<PropertyPhotoResponse> photos;
	private List<ContactPersonResponse> contacts;
	


	public List<ContactPersonResponse> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactPersonResponse> contacts) {
		this.contacts = contacts;
	}

	public PgResponse() {
	}

	public PgResponse(Long id, String name, String type, Double price, String rules, Boolean availability,
			Address address, Long ownerId, String ownerName, String ownerEmail, List<FloorResponse> floors,
			List<AmenityResponse> amenities, List<PropertyPhotoResponse> photos,List<ContactPersonResponse> contactResponses) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
		this.rules = rules;
		this.availability = availability;
		this.address = address;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.ownerEmail = ownerEmail;
		this.floors = floors;
		this.amenities = amenities;
		this.photos = photos;
		this.contacts= contactResponses;
	}

	public PgResponse(Long id2, String name2, String type2, Double price2, String rules2, Boolean availability2,
			Address address2, Long long1, Object object, Object object2, List<FloorResponse> floorResponses,
			List<AmenityResponse> amenityResponses, List<PropertyPhotoResponse> photoResponses,
			List<ContactPersonResponse> contactResponses, List<Object> of) {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public List<FloorResponse> getFloors() {
		return floors;
	}

	public void setFloors(List<FloorResponse> floors) {
		this.floors = floors;
	}

	public List<AmenityResponse> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<AmenityResponse> amenities) {
		this.amenities = amenities;
	}

	public List<PropertyPhotoResponse> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PropertyPhotoResponse> photos) {
		this.photos = photos;
	}


	// Getters and Setters omitted for brevity (keep as you have them)
}
