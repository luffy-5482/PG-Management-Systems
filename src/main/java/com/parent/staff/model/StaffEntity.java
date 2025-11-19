package com.parent.staff.model;

import com.parent.pg.model.PgEntity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "staff")
public class StaffEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fullName;

	@Column(unique = true, nullable = false)
	private String email;

	private String phone;

	private String password;

	@Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ROLE_STAFF'")
	private String role = "ROLE_STAFF";

	private String designation;

	private String joinDate;

	private String shiftTiming;

	private Boolean active = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pg_id", nullable = false)
	@JsonBackReference(value = "pg-staff")
	private PgEntity pg;

	public StaffEntity() {
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getShiftTiming() {
		return shiftTiming;
	}

	public void setShiftTiming(String shiftTiming) {
		this.shiftTiming = shiftTiming;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public PgEntity getPg() {
		return pg;
	}

	public void setPg(PgEntity pg) {
		this.pg = pg;
	}
}
