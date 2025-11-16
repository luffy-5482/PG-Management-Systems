package com.parent.pg.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_persons")
public class ContactPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "pg_id")
    private PgEntity pg;

    public ContactPerson() {}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Boolean getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	public PgEntity getPg() {
		return pg;
	}
	public void setPg(PgEntity pg) {
		this.pg = pg;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
