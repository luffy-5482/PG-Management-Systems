package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantAuthRequest;
import com.parent.tenant.dto.TenantAuthResponse;
import com.parent.tenant.service.TenantAuthService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant/auth")
@CrossOrigin("*")
public class TenantAuthController {

    private final TenantAuthService service;

    public TenantAuthController(TenantAuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public TenantAuthResponse login(@RequestBody TenantAuthRequest req) {
        return service.login(req);
    }
}
