package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantSubscriptionDto;
import com.parent.tenant.service.TenantSubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant/{tenantId}/subscription")
public class TenantSubscriptionController {

    private final TenantSubscriptionService subscriptionService;

    public TenantSubscriptionController(TenantSubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // ✅ GET current subscription status
    @GetMapping("/status")
    public ResponseEntity<TenantSubscriptionDto> getStatus(@PathVariable Long tenantId) {
        TenantSubscriptionDto dto = subscriptionService.getSubscriptionStatus(tenantId);
        return ResponseEntity.ok(dto);
    }

    // ✅ A: create or update subscription
    @PostMapping
    public ResponseEntity<TenantSubscriptionDto> createOrUpdate(
            @PathVariable Long tenantId,
            @RequestBody TenantSubscriptionDto request
    ) {
        TenantSubscriptionDto dto = subscriptionService.createOrUpdateSubscription(tenantId, request);
        return ResponseEntity.ok(dto);
    }

    // ✅ B: renew subscription (+1 month)
    @PostMapping("/renew")
    public ResponseEntity<TenantSubscriptionDto> renew(
            @PathVariable Long tenantId
    ) {
        TenantSubscriptionDto dto = subscriptionService.renewSubscription(tenantId);
        return ResponseEntity.ok(dto);
    }
}
