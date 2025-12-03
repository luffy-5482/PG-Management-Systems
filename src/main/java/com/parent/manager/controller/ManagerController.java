package com.parent.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.config.JwtService;
import com.parent.manager.dto.ManagerResponse;
import com.parent.manager.service.ManagerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {

    private final ManagerService service;
    private final JwtService jwtService;

    public ManagerController(ManagerService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    // ONLY LOGIN + SELF PROFILE

    @GetMapping("/me")
    public ResponseEntity<ManagerResponse> me(HttpServletRequest request) {
        Long managerId = jwtService.extractManagerIdFromRequest(request);
        if (managerId == null)
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok(service.getManager(managerId));
    }
}
