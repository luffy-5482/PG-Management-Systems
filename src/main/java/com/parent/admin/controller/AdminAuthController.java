package com.parent.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parent.admin.dto.AdminLoginRequest;
import com.parent.admin.service.AdminAuthService;
import com.parent.auth.AuthenticationResponse;

@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin(origins = "*")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AdminLoginRequest req) {
        return ResponseEntity.ok(adminAuthService.login(req.getEmail(), req.getPassword()));
    }
}
