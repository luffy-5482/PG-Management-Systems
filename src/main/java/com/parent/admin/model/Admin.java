package com.parent.admin.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "admins")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password; // encoded

	private Instant createdAt = Instant.now();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "admin_permissions", joinColumns = @JoinColumn(name = "admin_id"))
	@Column(name = "permission")
	private Set<String> permissions = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "admin_allowed_pgs", joinColumns = @JoinColumn(name = "admin_id"))
	@Column(name = "pg_id")
	private Set<Long> allowedPgIds = new HashSet<>();

	// Getters / Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public Set<Long> getAllowedPgIds() {
		return allowedPgIds;
	}

	public void setAllowedPgIds(Set<Long> allowedPgIds) {
		this.allowedPgIds = allowedPgIds;
	}
}
