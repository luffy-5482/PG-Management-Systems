package com.parent.staff.dto;

public class StaffRequest {

    public String fullName;
    public String phone;
    public String role;
    public Long pgId;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getPgId() {
		return pgId;
	}
	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}
    
}

