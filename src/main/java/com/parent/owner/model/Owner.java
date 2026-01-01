package com.parent.owner.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.parent.pg.model.PgEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "owners", uniqueConstraints = @UniqueConstraint(columnNames = { "email" }))
public class Owner implements UserDetails {

	@Id
	private Long id; 
	private String fullName;

	@Column(unique = true)
	private String email;

	private String phoneNumber;
	private String gender;

	@Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'ROLE_OWNER'")
	private String role = "ROLE_OWNER";

	@JsonIgnore
	@Column(nullable = false)
	private String password; // hashed password

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<PgEntity> pgs;

	public Owner() {
	}

	// Getters and Setters
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<PgEntity> getPgs() {
		return pgs;
	}

	public void setPgs(List<PgEntity> pgs) {
		this.pgs = pgs;
	}

	// ----- USERDETAILS METHODS -----

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.role));
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.email; // email used as login username
	}

//	@Override
//	@JsonIgnore
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	@JsonIgnore
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	@JsonIgnore
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	@JsonIgnore
//	public boolean isEnabled() {
//		return true;
//	}
}
