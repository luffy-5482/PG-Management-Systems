package com.parent.tenant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.tenant.dto.TenantDashboardResponse;
import com.parent.tenant.service.TenantDashboardService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tenant/dashboard")
public class TenantDashboardController {

    private final TenantDashboardService service;

    public TenantDashboardController(TenantDashboardService service) {
        this.service = service;
    }

    @GetMapping
    public TenantDashboardResponse dashboard(HttpServletRequest request) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        return service.getDashboard(tenantId);
    }
}
