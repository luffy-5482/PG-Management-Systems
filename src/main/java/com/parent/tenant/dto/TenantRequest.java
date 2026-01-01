package com.parent.tenant.dto;

public class TenantRequest {
	private long pgId;
	private String fname;
	private String lname;
	private String email;	
	private String password;
	private String phone;
	private String gender;
	private String occupation;
	private String address;
	private String emergency_contact_name;
	private String emergency_phone;
	private String emergency_address;
	private String relationship;
	private String city;
	private int pincode;
	
	
	public long getPgId() {
		return pgId;
	}
	public void setPgId(long pgId) {
		this.pgId = pgId;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmergency_contact_name() {
		return emergency_contact_name;
	}
	public void setEmergency_contact_name(String emergency_contact_name) {
		this.emergency_contact_name = emergency_contact_name;
	}
	public String getEmergency_phone() {
		return emergency_phone;
	}
	public void setEmergency_phone(String emergency_phone) {
		this.emergency_phone = emergency_phone;
	}
	public String getEmergency_address() {
		return emergency_address;
	}
	public void setEmergency_address(String emergency_address) {
		this.emergency_address = emergency_address;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPincode() {
		return pincode;
	}
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}
	
}
