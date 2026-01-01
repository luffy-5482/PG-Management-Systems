package com.parent.tenant.model;

import java.time.Instant;

import com.parent.tenant.enums.ApplicationStep;
import com.parent.tenant.enums.TenantStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "tenant_applications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
    }
)
public class TenantApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pgId;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String phone;


    @Enumerated(EnumType.STRING)
    private TenantStatus status;

    @Enumerated(EnumType.STRING)
    private ApplicationStep currentStep;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }


    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPgId() {
		return pgId;
	}

	public void setPgId(String pgId) {
		this.pgId = pgId;
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

	public TenantStatus getStatus() {
		return status;
	}

	public void setStatus(TenantStatus status) {
		this.status = status;
	}

	public ApplicationStep getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(ApplicationStep currentStep) {
		this.currentStep = currentStep;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

    // getters & setters (generate via IDE)
}
