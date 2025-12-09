package com.parent.tenant.model;

import com.parent.payment.model.Tenant;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_activity_log")
public class TenantActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // which tenant this activity belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private String type;   // PAYMENT, SUPPORT_TICKET, PROFILE, DOCUMENT, SUBSCRIPTION

    @Column(nullable = false)
    private String title;  // short label, e.g. "Subscription renewed"

    @Column(columnDefinition = "text")
    private String description;   // optional details

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public TenantActivityLog() {
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // -------- getters & setters --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
