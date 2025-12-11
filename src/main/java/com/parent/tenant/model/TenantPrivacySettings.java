package com.parent.tenant.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tenant_privacy_settings")
public class TenantPrivacySettings {

    @Id
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "show_phone", nullable = false)
    private Boolean showPhone = Boolean.FALSE;

    @Column(name = "show_email", nullable = false)
    private Boolean showEmail = Boolean.TRUE;

    @Column(name = "show_profile", nullable = false)
    private Boolean showProfile = Boolean.TRUE;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    // getters/setters

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Boolean getShowPhone() { return showPhone; }
    public void setShowPhone(Boolean showPhone) { this.showPhone = showPhone; }

    public Boolean getShowEmail() { return showEmail; }
    public void setShowEmail(Boolean showEmail) { this.showEmail = showEmail; }

    public Boolean getShowProfile() { return showProfile; }
    public void setShowProfile(Boolean showProfile) { this.showProfile = showProfile; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
