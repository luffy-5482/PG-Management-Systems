package com.parent.tenant.dto;

public class NotificationPrefsDto {
    private Boolean paymentAlerts;
    private Boolean maintenanceAlerts;
    private Boolean noticeAlerts;
    private Boolean generalAlerts;

    public Boolean getPaymentAlerts() { return paymentAlerts; }
    public void setPaymentAlerts(Boolean paymentAlerts) { this.paymentAlerts = paymentAlerts; }

    public Boolean getMaintenanceAlerts() { return maintenanceAlerts; }
    public void setMaintenanceAlerts(Boolean maintenanceAlerts) { this.maintenanceAlerts = maintenanceAlerts; }

    public Boolean getNoticeAlerts() { return noticeAlerts; }
    public void setNoticeAlerts(Boolean noticeAlerts) { this.noticeAlerts = noticeAlerts; }

    public Boolean getGeneralAlerts() { return generalAlerts; }
    public void setGeneralAlerts(Boolean generalAlerts) { this.generalAlerts = generalAlerts; }
}
