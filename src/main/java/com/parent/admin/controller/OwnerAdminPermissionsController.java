package com.parent.admin.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.admin.dto.UpdatePermissionsRequest;
import com.parent.admin.model.Admin;
import com.parent.admin.repository.AdminRepository;

import jakarta.servlet.http.HttpServletRequest;

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
    @PutMapping("/{adminId}/permissions")
    public ResponseEntity<Set<String>> updatePermissions(
            @PathVariable Long adminId,
            @RequestBody UpdatePermissionsRequest req,
            HttpServletRequest request) {

        if (!isOwner(request))
            return ResponseEntity.status(403).build();

        if (req.permissions == null)
            return ResponseEntity.badRequest().body(null);

        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 💥 Replace the entire permission set
        admin.setPermissions(new HashSet<>(req.permissions));

        adminRepo.save(admin);

        return ResponseEntity.ok(admin.getPermissions());
    }

}
