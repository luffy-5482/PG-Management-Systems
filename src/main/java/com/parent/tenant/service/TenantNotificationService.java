package com.parent.tenant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parent.tenant.model.TenantNotification;
import com.parent.tenant.repository.TenantNotificationRepository;

import java.util.List;

@Service
public class TenantNotificationService {

    private final TenantNotificationRepository repo;

    public TenantNotificationService(TenantNotificationRepository repo) {
        this.repo = repo;
    }

    public List<TenantNotification> getNotifications(Long tenantId) {
        return repo.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long tenantId) {

        TenantNotification notification = repo.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getTenantId().equals(tenantId))
            throw new IllegalStateException("Unauthorized");

        notification.setRead(true);
        repo.save(notification);
    }

    // Utility method (used by other modules)
    public void create(Long tenantId, String title, String message) {
        TenantNotification n = new TenantNotification();
        n.setTenantId(tenantId);
        n.setTitle(title);
        n.setMessage(message);
        repo.save(n);
    }
}
