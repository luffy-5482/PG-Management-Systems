package com.parent.tenant.model;

import com.parent.tenant.enums.TenantStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tenant_accounts")
public class TenantAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Long applicationId;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password; // encoded later

	@Enumerated(EnumType.STRING)
	private TenantStatus status;

	private Instant createdAt;

	// In com.parent.tenant.model.TenantAccount.java

	// Add this field
	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean tempPassword = true; // default true when created in approval

	// Add getters and setters
	public boolean isTempPassword() {
		return tempPassword;
	}

	public void setTempPassword(boolean tempPassword) {
		this.tempPassword = tempPassword;
	}

	@PrePersist
	public void onCreate() {
		this.createdAt = Instant.now();
		this.status = TenantStatus.ACTIVE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
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

	public TenantStatus getStatus() {
		return status;
	}

	public void setStatus(TenantStatus status) {
		this.status = status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
