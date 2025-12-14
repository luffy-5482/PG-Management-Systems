package com.parent.staff.controller;

import com.parent.config.JwtService;
import com.parent.staff.dto.StaffRequest;
import com.parent.staff.dto.StaffResponse;
import com.parent.staff.service.StaffService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService service;
    private final JwtService jwtService;

    public StaffController(StaffService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/create") 
    public ResponseEntity<StaffResponse> create(@RequestBody StaffRequest req, HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        Long managerId = jwtService.extractManagerIdFromRequest(request);

        if (ownerId == null && managerId == null)
            return ResponseEntity.status(403).build();

        if (managerId != null) {
            if (!service.managerCanAccessPg(managerId, req.getPgId())) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(service.createStaff(req, managerId, ownerId));
    }

    // Owner or Manager can update staff (Manager only within his allowed PGs)
    @PutMapping("/{id}")
    public ResponseEntity<StaffResponse> update(
            @PathVariable Long id,
            @RequestBody StaffRequest req,
            HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        Long managerId = jwtService.extractManagerIdFromRequest(request);

        if (ownerId == null && managerId == null)
            return ResponseEntity.status(403).build();

        // Manager PG restriction - service will also check but double-check here for clearer 403
        if (managerId != null) {
            StaffResponse existing = service.getStaff(id);
            if (!service.managerCanAccessPg(managerId, existing.getPgId())) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(service.updateStaff(id, req, managerId, ownerId));
    }

    // Owner OR Manager can delete staff (Manager only within his allowed PGs)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        Long managerId = jwtService.extractManagerIdFromRequest(request);

        if (ownerId == null && managerId == null)
            return ResponseEntity.status(403).build();

        // Manager PG restriction - check before calling service
        if (managerId != null) {
            StaffResponse existing = service.getStaff(id);
            if (!service.managerCanAccessPg(managerId, existing.getPgId())) {
                return ResponseEntity.status(403).build();
            }
        }

        service.deleteStaff(id, managerId, ownerId);
        return ResponseEntity.noContent().build();
    }

    // Owner and Manager can view one staff (Manager only within his allowed PGs)
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getOne(@PathVariable Long id, HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        Long managerId = jwtService.extractManagerIdFromRequest(request);

        if (ownerId == null && managerId == null)
            return ResponseEntity.status(403).build();

        StaffResponse s = service.getStaff(id);

        // Manager PG restriction
        if (managerId != null) {
            if (!service.managerCanAccessPg(managerId, s.getPgId())) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(s);
    }

    // Owner and Manager can view staff by PG (Manager only for his allowed PGs)
    @GetMapping("/pg/{pgId}")
    public ResponseEntity<List<StaffResponse>> getByPg(
            @PathVariable Long pgId,
            HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        Long managerId = jwtService.extractManagerIdFromRequest(request);

        if (ownerId == null && managerId == null)
            return ResponseEntity.status(403).build();

        // Manager PG restriction
        if (managerId != null) {
            if (!service.managerCanAccessPg(managerId, pgId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(service.getStaffByPg(pgId));
    }
}
