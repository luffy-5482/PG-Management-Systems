package com.parent.tenant.dto;

import java.time.LocalDate;

public class TenantSubscriptionDto {

    // which tenant this subscription belongs to
    private Long tenantId;

    // subscription details
    private Double monthlyRent;   // 👈 Double, not Integer
    private LocalDate nextDueDate;
    private String status;        // e.g. ACTIVE / BLOCKED / PENDING

    // extra info for UI
    private String message;

    // ----------------- getters & setters -----------------

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Double getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(Double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
