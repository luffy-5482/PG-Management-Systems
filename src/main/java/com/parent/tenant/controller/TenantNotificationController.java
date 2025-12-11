package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantNotificationDto;
import com.parent.tenant.dto.CreateNotificationRequest;
import com.parent.tenant.service.TenantNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/{tenantId}/notifications")
public class TenantNotificationController {

    private final TenantNotificationService service;

    public TenantNotificationController(TenantNotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TenantNotificationDto>> getNotifications(@PathVariable Long tenantId) {
        return ResponseEntity.ok(service.getNotificationsForTenant(tenantId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long tenantId) {
        return ResponseEntity.ok(service.getUnreadCount(tenantId));
    }

    @PostMapping("/{id}/mark-read")
    public ResponseEntity<Void> markRead(@PathVariable Long tenantId, @PathVariable Long id) {
        service.markAsRead(tenantId, id);
        return ResponseEntity.noContent().build();
    }

    // Owner/Admin endpoint to create notification for a tenant (can be moved to owner controller)
    @PostMapping("/create") // e.g. /api/tenant/{tenantId}/notifications/create
    public ResponseEntity<TenantNotificationDto> create(@PathVariable Long tenantId,
                                                        @RequestBody CreateNotificationRequest req) {
        // ensure tenantId path and body match (optional)
        req.setTenantId(tenantId);
        TenantNotificationDto created = service.createNotification(req);
        return ResponseEntity.ok(created);
    }
}
