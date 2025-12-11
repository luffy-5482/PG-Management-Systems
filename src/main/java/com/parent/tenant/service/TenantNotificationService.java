package com.parent.tenant.service;

import com.parent.tenant.dto.TenantNotificationDto;
import com.parent.tenant.dto.CreateNotificationRequest;

import java.util.List;

public interface TenantNotificationService {
    List<TenantNotificationDto> getNotificationsForTenant(Long tenantId);
    long getUnreadCount(Long tenantId);
    void markAsRead(Long tenantId, Long notificationId);
    TenantNotificationDto createNotification(CreateNotificationRequest req); // owner/admin use
}
