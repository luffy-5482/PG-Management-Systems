package com.parent.tenant.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.parent.payment.model.Tenant; // existing Tenant entity in project

@Entity
@Table(name = "tenant_credentials")
public class TenantCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to tenant
    @OneToOne
    @JoinColumn(name = "tenant_id", unique = true)
    private Tenant tenant;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // store hashed password
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public TenantCredentials() {}

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
