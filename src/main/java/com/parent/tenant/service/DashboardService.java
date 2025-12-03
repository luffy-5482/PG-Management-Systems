package com.parent.tenant.service;

import com.parent.tenant.dto.DashboardResponse;

public interface DashboardService {
    /**
     * Get dashboard data for the given tenantId.
     * If tenantId is null behavior may be to return default or aggregated data.
     */
    DashboardResponse getDashboardData(Long tenantId);

    /**
     * Convenience method expected by the controller in this codebase.
     * Implementations may delegate to getDashboardData.
     */
    DashboardResponse getDashboardForTenant(Long tenantId);
}
