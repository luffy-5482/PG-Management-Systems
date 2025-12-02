package com.parent.staff.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/staff/dashboard")
@CrossOrigin(origins = "*")
public class StaffDashboardController {

    @GetMapping("/overview")
    public ResponseEntity<String> overview() {
        return ResponseEntity.ok("STAFF DASHBOARD OVERVIEW");
    }

    @GetMapping("/tasks")
    public ResponseEntity<String> tasks() {
        return ResponseEntity.ok("TASK LIST");
    }

    @GetMapping("/complaints")
    public ResponseEntity<String> complaints() {
        return ResponseEntity.ok("COMPLAINT LIST");
    }

    @GetMapping("/requests")
    public ResponseEntity<String> requests() {
        return ResponseEntity.ok("REQUEST LIST");
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        return ResponseEntity.ok("STAFF PROFILE INFO");
    }
}
