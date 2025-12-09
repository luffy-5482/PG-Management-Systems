package com.parent.tenant.dto;

import java.time.LocalDateTime;

public class TenantActivityDto {

    private String type;        // "PAYMENT" or "TICKET"
    private String title;       // main text shown in bold
    private String subtitle;    // e.g. "On 8 Dec 2025 · via Razorpay"
    private String status;      // e.g. SUCCESS, OPEN, CLOSED
    private Double amount;      // only for payments (null for tickets)
    private String icon;        // "payment" / "ticket" (FE maps to icon)
    private LocalDateTime createdAt;

    // ---------- getters & setters ----------

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
