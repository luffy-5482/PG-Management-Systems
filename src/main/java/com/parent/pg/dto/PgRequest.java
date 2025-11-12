package com.parent.pg.dto;

public class PgRequest {
	private Long ownerId;
	private String name;
	private String type;
	private Double price;
	private String rules;
	private Boolean availability;
	// Address (embedded)
	private String street;
	private String city;
	private String state;
	private String pincode;

	private ContactPersonRequest contactPerson;
	
	public PgRequest() {
	}

	// getters & setters
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public ContactPersonRequest getContactPerson() {
        return contactPerson;
    }
    public void setContactPerson(ContactPersonRequest contactPerson) {
        this.contactPerson = contactPerson;
    }

}
