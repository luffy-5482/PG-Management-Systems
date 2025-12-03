package com.parent.admin.controller;

import com.parent.admin.model.Admin;
import com.parent.admin.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/owner/admin")
public class OwnerAdminPermissionsController {

    private final AdminRepository adminRepo;

    public OwnerAdminPermissionsController(AdminRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    // ✔ CHECK IF LOGGED-IN USER IS OWNER
    private boolean isOwner(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        return role != null && role.equals("OWNER");
    }

    // ---------------------------------------------------------
    // 1️⃣ GET ADMIN’S CURRENT PERMISSIONS
    // ---------------------------------------------------------
    @GetMapping("/{adminId}/permissions")
    public ResponseEntity<Set<String>> getAdminPermissions(
            @PathVariable Long adminId,
            HttpServletRequest request) {

        if (!isOwner(request))
            return ResponseEntity.status(403).build();

        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return ResponseEntity.ok(admin.getPermissions());  // ✔ returns Set<String>
    }

    // ---------------------------------------------------------
    // 2️⃣ ADD ONE PERMISSION (checkbox checked)
    // ---------------------------------------------------------
    @PostMapping("/{adminId}/permissions/add")
    public ResponseEntity<Set<String>> addPermission(
            @PathVariable Long adminId,
            @RequestBody(required = true) String permission,
            HttpServletRequest request) {

        if (!isOwner(request))
            return ResponseEntity.status(403).build();

        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.getPermissions().add(permission); // ✔ add string
        adminRepo.save(admin);

        return ResponseEntity.ok(admin.getPermissions());
    }

    // ---------------------------------------------------------
    // 3️⃣ REMOVE ONE PERMISSION (checkbox unchecked)
    // ---------------------------------------------------------
    @PostMapping("/{adminId}/permissions/remove")
    public ResponseEntity<Set<String>> removePermission(
            @PathVariable Long adminId,
            @RequestBody(required = true) String permission,
            HttpServletRequest request) {

        if (!isOwner(request))
            return ResponseEntity.status(403).build();

        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.getPermissions().remove(permission); // ✔ remove string
        adminRepo.save(admin);

        return ResponseEntity.ok(admin.getPermissions());
    }
}
