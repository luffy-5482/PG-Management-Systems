package com.parent.owner.dto.copy;

import java.util.List;	
import com.parent.pg.dto.PgResponse;

public class OwnerResponse {
	private Long id;
	private String fullName;
	private String email;
	private String phoneNumber;
	private String gender;
	private List<PgResponse> pgs; // nested PGs with full subparts

	public OwnerResponse() {
	}

	public OwnerResponse(Long id, String fullName, String email, String phoneNumber, String gender,
			List<PgResponse> pgs) {
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.pgs = pgs;
	}

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<PgResponse> getPgs() {
		return pgs;
	}

	public void setPgs(List<PgResponse> pgs) {
		this.pgs = pgs;
	}
}
