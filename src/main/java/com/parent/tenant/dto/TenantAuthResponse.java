package com.parent.tenant.dto;

public class TenantAuthResponse {
    public String token;
    public Long tenantId;
    public String name;
    public Long roomId;
    public Long pgId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getPgId() {
		return pgId;
	}
	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}
    
}
