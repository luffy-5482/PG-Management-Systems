package com.parent.tenant.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tenant_notification_prefs")
public class TenantNotificationPrefs {

    @Id
    @Column(name = "tenant_id")
    private Long tenantId; // PK (and logical FK to tenants.id)

    @Column(name = "payment_alerts", nullable = false)
    private boolean paymentAlerts = true;

    @Column(name = "maintenance_alerts", nullable = false)
    private boolean maintenanceAlerts = true;

    @Column(name = "notice_alerts", nullable = false)
    private boolean noticeAlerts = true;

    @Column(name = "general_alerts", nullable = false)
    private boolean generalAlerts = true;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public TenantNotificationPrefs() {}

    // --- getters & setters (bean-style names expected by service) ---
    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public boolean getPaymentAlerts() { return paymentAlerts; }
    public void setPaymentAlerts(boolean paymentAlerts) { this.paymentAlerts = paymentAlerts; }

    public boolean getMaintenanceAlerts() { return maintenanceAlerts; }
    public void setMaintenanceAlerts(boolean maintenanceAlerts) { this.maintenanceAlerts = maintenanceAlerts; }

    public boolean getNoticeAlerts() { return noticeAlerts; }
    public void setNoticeAlerts(boolean noticeAlerts) { this.noticeAlerts = noticeAlerts; }

    public boolean getGeneralAlerts() { return generalAlerts; }
    public void setGeneralAlerts(boolean generalAlerts) { this.generalAlerts = generalAlerts; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
