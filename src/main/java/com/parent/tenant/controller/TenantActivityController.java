package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantActivityDto;
import com.parent.tenant.service.TenantActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/{tenantId}/activity")
public class TenantActivityController {

    private final TenantActivityService activityService;

    public TenantActivityController(TenantActivityService activityService) {
        this.activityService = activityService;
    }

    // GET /api/tenant/1/activity
    @GetMapping
    public ResponseEntity<List<TenantActivityDto>> getActivity(@PathVariable Long tenantId) {
        return ResponseEntity.ok(activityService.getActivityForTenant(tenantId));
    }
}
