package com.parent.manager.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.config.JwtService;
import com.parent.manager.model.Attendance;
import com.parent.manager.service.AttendanceService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/manager/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService service;
    private final JwtService jwtService;

    public AttendanceController(AttendanceService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    // ---------------------------------------------------------
    // MARK ATTENDANCE
    // ---------------------------------------------------------
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(
            @RequestBody Attendance req,
            HttpServletRequest request) {

        Long managerId = jwtService.extractManagerIdFromRequest(request);
        if (managerId == null)
            return ResponseEntity.status(403).body("NOT MANAGER");

        // 1) Fetch allowed PGs from token
        Set<Long> allowed = jwtService.extractAllowedPgIdsFromRequest(request);

        // 2) Check if this manager is allowed to access this PG
        if (!allowed.contains(req.getPgId())) {
            return ResponseEntity.status(403)
                    .body("Manager does NOT have access to PG ID: " + req.getPgId());
        }

        // 3) Manager is valid → save attendance
        req.setManagerId(managerId);
        req.setDate(LocalDate.now());
        req.setCheckIn(LocalTime.now());

        return ResponseEntity.ok(service.markCheckIn(req));
    }


    // ---------------------------------------------------------
    // CHECK OUT
    // ---------------------------------------------------------
    @PatchMapping("/checkout/{attendanceId}")
    public ResponseEntity<?> checkOut(
            @PathVariable Long attendanceId,
            HttpServletRequest request) {

        Long managerId = jwtService.extractManagerIdFromRequest(request);
        if (managerId == null)
            return ResponseEntity.status(403).body("NOT MANAGER");

        return ResponseEntity.ok(service.markCheckOut(attendanceId, LocalTime.now()));
    }

    // ---------------------------------------------------------
    // GET ATTENDANCE FOR TODAY
    // ---------------------------------------------------------
    @GetMapping("/today")
    public ResponseEntity<?> todaysAttendance(HttpServletRequest request) {

        Long managerId = jwtService.extractManagerIdFromRequest(request);
        if (managerId == null)
            return ResponseEntity.status(403).body("NOT MANAGER");

        return ResponseEntity.ok(service.getByManagerAndDate(managerId, LocalDate.now()));
    }
}
