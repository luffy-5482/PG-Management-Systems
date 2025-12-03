package com.parent.admin.service;

import com.parent.admin.dto.AdminResponse;
import com.parent.admin.dto.CreateAdminRequest;
import com.parent.admin.dto.UpdateAdminRequest;
import com.parent.admin.model.Admin;
import com.parent.admin.model.Permission;
import com.parent.admin.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminResponse createAdmin(CreateAdminRequest req, Long ownerId) {
        Optional<Admin> existing = adminRepo.findByEmail(req.email);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        Admin a = new Admin();
        a.setName(req.name);
        a.setEmail(req.email);
        a.setPassword(passwordEncoder.encode(req.password));
        if (req.permissions != null) a.setPermissions(req.permissions);
        if (req.allowedPgIds != null) a.setAllowedPgIds(req.allowedPgIds);
        Admin saved = adminRepo.save(a);
        return toResponse(saved);
    }

    public AdminResponse updateAdmin(Long id, UpdateAdminRequest req, Long ownerId) {
        Admin a = adminRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        if (req.name != null) a.setName(req.name);
        if (req.permissions != null) a.setPermissions(req.permissions);
        if (req.allowedPgIds != null) a.setAllowedPgIds(req.allowedPgIds);
        Admin saved = adminRepo.save(a);
        return toResponse(saved);
    }

    public void deleteAdmin(Long id, Long ownerId) {
        adminRepo.deleteById(id);
    }

    public AdminResponse getAdmin(Long id) {
        return toResponse(adminRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Admin not found")));
    }

    public List<AdminResponse> getAllAdmins() {
        return adminRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Permission check. Returns true if admin has the requested permission (and PG access if ACCESS_LIMITED_TO_PG present).
     * If admin has FULL_ACCESS, returns true.
     */
    public boolean hasPermission(Long adminId, Permission permission, Long pgId) {
        Admin admin = adminRepo.findById(adminId).orElse(null);
        if (admin == null) return false;
        Set<String> perms = admin.getPermissions();
        if (perms.contains(Permission.FULL_ACCESS.name())) return true;
        if (permission == null) return false;
        if (perms.contains(permission.name())) {
            if (perms.contains(Permission.ACCESS_LIMITED_TO_PG.name())) {
                return admin.getAllowedPgIds() != null && admin.getAllowedPgIds().contains(pgId);
            }
            return true;
        }
        return false;
    }

    private AdminResponse toResponse(Admin a) {
        AdminResponse r = new AdminResponse();
        r.id = a.getId();
        r.name = a.getName();
        r.email = a.getEmail();
        r.permissions = a.getPermissions();
        r.allowedPgIds = a.getAllowedPgIds();
        r.createdAt = a.getCreatedAt();
        return r;
    }
}
