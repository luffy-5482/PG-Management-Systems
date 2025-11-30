package com.parent.staff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parent.staff.dto.StaffRequest;
import com.parent.staff.dto.StaffResponse;
import com.parent.staff.service.StaffService;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService service;

    public StaffController(StaffService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StaffResponse> create(@RequestBody StaffRequest req) {
        return ResponseEntity.ok(service.createStaff(req));
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<StaffResponse> update(@PathVariable Long id, @RequestBody StaffRequest req) {
        return ResponseEntity.ok(service.updateStaff(id, req));
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteStaff(id);
        return ResponseEntity.ok("Staff deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStaff(id));
    }

    @GetMapping("/pgs/{pgId}")
    public ResponseEntity<List<StaffResponse>> getByPg(@PathVariable Long pgId) {
        return ResponseEntity.ok(service.getStaffByPg(pgId));
    }
}
