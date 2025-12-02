package com.parent.staff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.staff.dto.StaffLoginRequest;
import com.parent.staff.dto.StaffLoginResponse;
import com.parent.staff.service.StaffAuthService;

@RestController
@RequestMapping("/api/staff/auth")
@CrossOrigin(origins = "*")
public class StaffAuthController {

    private final StaffAuthService staffAuthService;

    public StaffAuthController(StaffAuthService staffAuthService) {
        this.staffAuthService = staffAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<StaffLoginResponse> login(@RequestBody StaffLoginRequest req) {
        return ResponseEntity.ok(staffAuthService.login(req));
    }
}
