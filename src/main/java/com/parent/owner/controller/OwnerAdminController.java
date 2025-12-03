package com.parent.owner.controller;

import com.parent.admin.dto.AdminResponse;	
import com.parent.admin.dto.CreateAdminRequest;
import com.parent.admin.dto.UpdateAdminRequest;
import com.parent.admin.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/owner/admin")
@CrossOrigin(origins = "*")
public class OwnerAdminController {

    private final AdminService adminService;
    private final com.parent.config.JwtService jwtService;

    public OwnerAdminController(AdminService adminService, com.parent.config.JwtService jwtService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody CreateAdminRequest req, HttpServletRequest request) {
        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null || !jwtService.extractRoleFromRequest(request).equalsIgnoreCase("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AdminResponse res = adminService.createAdmin(req, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Long id, @RequestBody UpdateAdminRequest req, HttpServletRequest request) {
        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null || !jwtService.extractRoleFromRequest(request).equalsIgnoreCase("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AdminResponse res = adminService.updateAdmin(id, req, ownerId);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<AdminResponse>> listAdmins(HttpServletRequest request) {
        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null || !jwtService.extractRoleFromRequest(request).equalsIgnoreCase("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}") 
    public ResponseEntity<AdminResponse> getAdmin(@PathVariable Long id, HttpServletRequest request) {
        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null || !jwtService.extractRoleFromRequest(request).equalsIgnoreCase("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(adminService.getAdmin(id)); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id, HttpServletRequest request) {
        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null || !jwtService.extractRoleFromRequest(request).equalsIgnoreCase("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        adminService.deleteAdmin(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}
