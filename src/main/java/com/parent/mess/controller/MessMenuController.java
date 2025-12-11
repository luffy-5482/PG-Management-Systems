package com.parent.mess.controller;

import com.parent.mess.dto.MessMenuDto;
import com.parent.mess.service.MessMenuService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class MessMenuController {

    private final MessMenuService service;

    public MessMenuController(MessMenuService service) {
        this.service = service;
    }

    // ---------------- tenant read endpoints ----------------
    // GET /api/tenant/mess/menu?date=2025-12-10
    @GetMapping("/api/tenant/mess/menu")
    public ResponseEntity<MessMenuDto> getMenuForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        MessMenuDto dto = service.getMenuByDate(date);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    // GET /api/tenant/mess/menu/week?from=2025-12-07&to=2025-12-13
    @GetMapping("/api/tenant/mess/menu/week")
    public ResponseEntity<List<MessMenuDto>> getMenusInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(service.getMenusForRange(from, to));
    }

    // ---------------- owner endpoints (create/update/delete) ----------------
    // POST /api/owner/mess/menu
    @PostMapping("/api/owner/mess/menu")
    public ResponseEntity<MessMenuDto> createOrUpdateMenu(@RequestBody MessMenuDto dto) {
        // Security: this path should be allowed only for OWNER by SecurityConfiguration
        MessMenuDto saved = service.createOrUpdateMenu(dto);
        return ResponseEntity.ok(saved);
    }

    // DELETE /api/owner/mess/menu?date=2025-12-10
    @DeleteMapping("/api/owner/mess/menu")
    public ResponseEntity<Void> deleteMenu(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        service.deleteMenu(date);
        return ResponseEntity.noContent().build();
    }
}
