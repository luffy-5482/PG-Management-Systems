package com.parent.owner.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.config.JwtService;
import com.parent.manager.dto.CreateManagerRequest;
import com.parent.manager.dto.ManagerResponse;
import com.parent.manager.dto.UpdateManagerRequest;
import com.parent.manager.service.ManagerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/owner/managers")
@CrossOrigin(origins = "*")
public class OwnerManagerController {

    private final ManagerService managerService;
    private final JwtService jwtService;

    public OwnerManagerController(ManagerService managerService, JwtService jwtService) {
        this.managerService = managerService;
        this.jwtService = jwtService;
    }

    // ---------------------------------------------------------
    // CREATE MANAGER
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(
            @RequestBody CreateManagerRequest req,
            HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ManagerResponse res = managerService.createManager(req, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}") 
    public ResponseEntity<ManagerResponse> updateManager(
            @PathVariable Long id,
            @RequestBody UpdateManagerRequest req,
            HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(managerService.updateManager(id, req, ownerId));
    }

    // ---------------------------------------------------------
    // GET ALL MANAGERS
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ManagerResponse>> listManagers(HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(managerService.listManagers());
    }

    // ---------------------------------------------------------
    // GET MANAGER BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> getManager(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(managerService.getManager(id));
    }

    // ---------------------------------------------------------
    // DELETE MANAGER
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        if (ownerId == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        managerService.deleteManager(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}
