package com.parent.tenant.service;

import org.springframework.stereotype.Service;

import com.parent.tenant.dto.TenantDashboardResponse;
import com.parent.tenant.model.TenantAccount;
import com.parent.tenant.model.TenantApplication;
import com.parent.tenant.repository.TenantAccountRepository;
import com.parent.tenant.repository.TenantApplicationRepository;
import com.parent.tenant.repository.TenantNotificationRepository;

@Service
public class TenantDashboardService {

    private final TenantAccountRepository accountRepo;
    private final TenantApplicationRepository appRepo;
    private final TenantNotificationRepository notificationRepo;

    public TenantDashboardService(
            TenantAccountRepository accountRepo,
            TenantApplicationRepository appRepo,
            TenantNotificationRepository notificationRepo
    ) {
        this.accountRepo = accountRepo;
        this.appRepo = appRepo;
        this.notificationRepo = notificationRepo;
    }

    public TenantDashboardResponse getDashboard(Long tenantAccountId) {

        TenantAccount account = accountRepo.findById(tenantAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tenant"));

        TenantApplication app = appRepo.findById(account.getApplicationId())
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        TenantDashboardResponse res = new TenantDashboardResponse();
        res.setEmail(account.getEmail());
        res.setAccountStatus(account.getStatus());
        res.setApplicationStatus(app.getStatus());
        res.setUnreadNotifications(
                notificationRepo.countByTenantIdAndReadFalse(tenantAccountId)
        );

        return res;
    }
}
