package com.parent.manager.dto;

import java.time.Instant;
import java.util.Set;

public class ManagerResponse {
    public Long id;
    public String fullName;
    public String email;
    public String phone;
    public Set<Long> allowedPgIds;
    public Instant createdAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
    
}
