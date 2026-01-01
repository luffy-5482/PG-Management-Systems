package com.parent.tenant.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.tenant.enums.SupportCategory;
import com.parent.tenant.model.TenantSupportTicket;
import com.parent.tenant.service.TenantSupportService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/api/tenant/self/support")
public class TenantSupportController {

    private final TenantSupportService service;

    public TenantSupportController(TenantSupportService service) {
        this.service = service;
    }

    @PostMapping
    public TenantSupportTicket create(
            HttpServletRequest request,
            @RequestBody Map<String, String> body
    ) {

        Long tenantId = (Long) request.getAttribute("tenantId");
        Long pgId = (Long) request.getAttribute("pgId");

        return service.createTicket(
                tenantId,
                pgId,
                SupportCategory.valueOf(body.get("category")),
                body.get("subject"),
                body.get("description")
        );
    }

    @GetMapping
    public List<TenantSupportTicket> myTickets(HttpServletRequest request) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        return service.myTickets(tenantId);
    }
}
