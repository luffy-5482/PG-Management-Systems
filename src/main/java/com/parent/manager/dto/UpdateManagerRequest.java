package com.parent.manager.dto;

import java.util.Set;

public class UpdateManagerRequest {
    public String fullName;
    public String phone;
    public Set<Long> allowedPgIds;
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
    
}
