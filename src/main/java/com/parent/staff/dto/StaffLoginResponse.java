package com.parent.staff.dto;

public class StaffLoginResponse {

	private String token;
	private String refreshToken;
	private Long staffId;
	private Long pgId;
	private String role;

	public StaffLoginResponse() {
	}

	public StaffLoginResponse(String token, String refreshToken, Long staffId, Long pgId, String role) {
		this.token = token;
		this.refreshToken = refreshToken;
		this.staffId = staffId;
		this.pgId = pgId;
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Long getStaffId() {
		return staffId;
	}

	public Long getPgId() {
		return pgId;
	}

	public String getRole() {
		return role;
	}
}
