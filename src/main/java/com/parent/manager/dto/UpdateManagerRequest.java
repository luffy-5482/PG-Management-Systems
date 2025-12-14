package com.parent.manager.dto;

import java.util.Set;

public class UpdateManagerRequest {
    public String fullName;
    public String phone;
    public Set<Long> allowedPgIds;
    public String email; 
    public String password;
    
    
    
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
	public Set<Long> getAllowedPgIds() {
		return allowedPgIds;
	}
	public void setAllowedPgIds(Set<Long> allowedPgIds) {
		this.allowedPgIds = allowedPgIds;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
}
