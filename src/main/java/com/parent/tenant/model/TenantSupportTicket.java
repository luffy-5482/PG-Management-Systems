package com.parent.tenant.model;

import java.time.Instant;

import com.parent.tenant.enums.SupportCategory;
import com.parent.tenant.enums.SupportStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "tenant_support_tickets")
public class TenantSupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;
    private Long pgId;

    @Enumerated(EnumType.STRING)
    private SupportCategory category;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private SupportStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.status = SupportStatus.OPEN;
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

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getPgId() {
		return pgId;
	}

	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}

	public SupportCategory getCategory() {
		return category;
	}

	public void setCategory(SupportCategory category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SupportStatus getStatus() {
		return status;
	}

	public void setStatus(SupportStatus status) {
		this.status = status;
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

    
}
