package com.parent.tenant.service;

import com.parent.tenant.dto.TenantActivityDto;

import java.util.List;

public interface TenantActivityService {

    // For dashboard timeline
    List<TenantActivityDto> getActivityForTenant(Long tenantId);

    // For other services to record events (payments, tickets, etc.)
    void recordActivity(Long tenantId, String type, String title, String description);
}
