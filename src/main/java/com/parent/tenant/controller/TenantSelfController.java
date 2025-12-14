package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantResponse;
import com.parent.tenant.model.TenantEntity;
import com.parent.tenant.repository.TenantRepository;
import com.parent.tenant.service.TenantService;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.model.RoomEntity;
import com.parent.pg.service.RoomService;
import com.parent.pg.service.PgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tenant")
@CrossOrigin("*")
public class TenantSelfController {

    @Autowired
    private TenantRepository tenantRepo;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PgService pgService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long getTenantId(HttpServletRequest req) {
        Object id = req.getAttribute("tenantId");
        if (id == null) throw new RuntimeException("Unauthorized");
        return Long.valueOf(String.valueOf(id));
    }

    // ------------------------------------------------------------------
    // 1️⃣ VIEW OWN PROFILE
    // ------------------------------------------------------------------
    @GetMapping("/me")
    public TenantResponse getProfile(HttpServletRequest req) {
        Long tenantId = getTenantId(req);
        return tenantService.getTenant(tenantId);
    }

    // ------------------------------------------------------------------
    // 2️⃣ VIEW ROOM DETAILS
    // ------------------------------------------------------------------
    @GetMapping("/room")
    public RoomResponse getRoomInfo(HttpServletRequest req) {
        Long tenantId = getTenantId(req);

        TenantEntity t = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        RoomEntity room = t.getRoom();
        return roomService.getRoomById(room.getId());
    }

    // ------------------------------------------------------------------
    // 3️⃣ VIEW PG DETAILS
    // ------------------------------------------------------------------
    @GetMapping("/pg")
    public Object getPgInfo(HttpServletRequest req) {
        Long tenantId = getTenantId(req);

        TenantEntity t = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        return pgService.getPgById(t.getPgId());
    }

    // ------------------------------------------------------------------
    // 4️⃣ CHANGE PASSWORD
    // ------------------------------------------------------------------
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            HttpServletRequest req,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        Long tenantId = getTenantId(req);

        TenantEntity t = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        if (!passwordEncoder.matches(oldPassword, t.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong old password");
        }

        t.setPassword(passwordEncoder.encode(newPassword));
        tenantRepo.save(t);

        return ResponseEntity.ok("Password updated successfully");
    }

    // ------------------------------------------------------------------
    // 5️⃣ REQUEST CHECKOUT
    // ------------------------------------------------------------------
    @PostMapping("/request-checkout")
    public ResponseEntity<String> requestCheckout(HttpServletRequest req) {
        Long tenantId = getTenantId(req);

        // For now soft-delete = checkout request
        tenantService.softDeleteTenant(tenantId, "TENANT");

        return ResponseEntity.ok("Checkout requested");
    }
}
