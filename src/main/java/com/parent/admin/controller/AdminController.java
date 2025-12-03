package com.parent.admin.controller;

import com.parent.admin.dto.AdminResponse;
import com.parent.admin.service.AdminService;
import com.parent.config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final JwtService jwtService;

    public AdminController(AdminService adminService, JwtService jwtService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    public ResponseEntity<AdminResponse> me(HttpServletRequest request) {
        Long adminId = jwtService.extractAdminIdFromRequest(request);
        if (adminId == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(adminService.getAdmin(adminId));
    }
}
