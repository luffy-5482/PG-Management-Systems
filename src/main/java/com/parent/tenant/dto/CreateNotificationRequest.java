package com.parent.tenant.dto;

public class CreateNotificationRequest {
    private Long tenantId;
    private String title;
    private String message;
    private String type; // PAYMENT, ALERT, MAINTENANCE, NOTICE

    // getters/setters
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
