package com.parent.tenant.service.impl;

import org.springframework.stereotype.Service;
import com.parent.tenant.dto.DashboardResponse;
import com.parent.tenant.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Override
    public DashboardResponse getDashboardData(Long tenantId) {
        DashboardResponse response = new DashboardResponse();
        response.setTotalBookings(0);
        response.setActiveBookings(0);
        response.setPendingPayments(0);
        response.setTotalPayments(0.0);
        response.setUpcomingRentDate(null);
        return response;
    }

    @Override
    public DashboardResponse getDashboardForTenant(Long tenantId) {
        // currently the same; replace with real DB logic later
        return getDashboardData(tenantId);
    }
}
