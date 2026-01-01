package com.parent.owner.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import com.parent.tenant.service.TenantApprovalService;

@RestController
@RequestMapping("/api/owner/tenants")
public class TenantApprovalController {

    private final TenantApprovalService service;

    public TenantApprovalController(TenantApprovalService service) {
        this.service = service;
    }

    @PostMapping("/{applicationId}/approve")
    public void approve(
            @PathVariable Long applicationId,
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) {
        service.approve(applicationId, body.get("tempPassword"), request);
    }

    @PostMapping("/{applicationId}/reject")
    public void reject(
            @PathVariable Long applicationId,
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) {
        service.reject(applicationId, body.get("reason"), request);
    }
}
