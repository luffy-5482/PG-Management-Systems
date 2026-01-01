package com.parent.tenant.service;
import org.springframework.stereotype.Service;

import com.parent.tenant.model.TenantActivityLog;
import com.parent.tenant.repository.TenantActivityLogRepository;

@Service
public class TenantActivityLogService {

    private final TenantActivityLogRepository repo;

    public TenantActivityLogService(TenantActivityLogRepository repo) {
        this.repo = repo;
    }

    public void log(Long applicationId, String action, String description) {
        TenantActivityLog log = new TenantActivityLog();
        log.setApplicationId(applicationId);
        log.setAction(action);
        log.setDescription(description);
        repo.save(log);
    }
}
