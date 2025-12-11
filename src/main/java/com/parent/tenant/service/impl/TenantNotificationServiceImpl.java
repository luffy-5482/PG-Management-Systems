package com.parent.tenant.service.impl;

import com.parent.tenant.dto.TenantNotificationDto;
import com.parent.tenant.dto.CreateNotificationRequest;
import com.parent.tenant.model.TenantNotification;
import com.parent.tenant.repository.TenantNotificationRepository;
import com.parent.tenant.service.TenantNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantNotificationServiceImpl implements TenantNotificationService {

    private final TenantNotificationRepository repo;

    public TenantNotificationServiceImpl(TenantNotificationRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<TenantNotificationDto> getNotificationsForTenant(Long tenantId) {
        List<TenantNotification> list = repo.findByTenantIdOrderByCreatedAtDesc(tenantId);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public long getUnreadCount(Long tenantId) {
        return repo.countByTenantIdAndIsReadFalse(tenantId);
    }

    @Override
    @Transactional
    public void markAsRead(Long tenantId, Long notificationId) {
        TenantNotification n = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!n.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Unauthorized");
        }
        n.setIsRead(true);
        repo.save(n);
    }

    @Override
    public TenantNotificationDto createNotification(CreateNotificationRequest req) {
        TenantNotification n = new TenantNotification();
        n.setTenantId(req.getTenantId());
        n.setTitle(req.getTitle());
        n.setMessage(req.getMessage());
        n.setType(req.getType());
        n.setIsRead(false);
        TenantNotification saved = repo.save(n);
        return toDto(saved);
    }

    private TenantNotificationDto toDto(TenantNotification n) {
        TenantNotificationDto d = new TenantNotificationDto();
        d.setId(n.getId());
        d.setTenantId(n.getTenantId());
        d.setTitle(n.getTitle());
        d.setMessage(n.getMessage());
        d.setType(n.getType());
        d.setIsRead(n.getIsRead());
        d.setCreatedAt(n.getCreatedAt());
        return d;
    }
}
