package com.parent.tenant.controller;

import com.parent.tenant.dto.DashboardResponse;
import com.parent.tenant.service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard/{tenantId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long tenantId) {
        DashboardResponse resp = dashboardService.getDashboardForTenant(tenantId);
        return ResponseEntity.ok(resp);
    }
}
