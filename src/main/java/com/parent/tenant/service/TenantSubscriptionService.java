package com.parent.tenant.service;

import com.parent.tenant.dto.TenantSubscriptionDto;

public interface TenantSubscriptionService {

    // existing
    TenantSubscriptionDto getSubscriptionStatus(Long tenantId);

    // ✅ A: create or update subscription
    TenantSubscriptionDto createOrUpdateSubscription(Long tenantId, TenantSubscriptionDto request);

    // ✅ B: renew subscription by 1 month
    TenantSubscriptionDto renewSubscription(Long tenantId);

    // Throws 403 (SubscriptionExpiredException) if tenant has no active / valid subscription.
    void assertSubscriptionActiveOrThrow(Long tenantId);

}

