package com.parent.owner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parent.config.SecurityUtils;
import com.parent.owner.dto.OwnerRequest;
import com.parent.owner.dto.OwnerResponse;
import com.parent.owner.service.OwnerService;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "*")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // ---------------------------------------------------------
    // 🔥 Get MY profile (logged-in owner only)
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<OwnerResponse> getMyProfile() {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();
        return ResponseEntity.ok(ownerService.getOwnerById(ownerId));
    }

    // ---------------------------------------------------------
    // 🔥 Update MY profile
    // ---------------------------------------------------------
    @PutMapping
    public ResponseEntity<OwnerResponse> updateMyProfile(@RequestBody OwnerRequest req) {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();
        return ResponseEntity.ok(ownerService.updateOwner(ownerId, req));
    }

    // ---------------------------------------------------------
    // (Optional) Delete MY account
    // ---------------------------------------------------------
    @DeleteMapping
    public ResponseEntity<String> deleteMyAccount() {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.ok("Owner account deleted successfully!");
    }
}
