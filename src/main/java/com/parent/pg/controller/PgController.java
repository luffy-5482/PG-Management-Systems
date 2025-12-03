package com.parent.pg.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.PgRequest;
import com.parent.pg.dto.PgResponse;
import com.parent.pg.service.PgService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/pgs")
@CrossOrigin(origins = "*")
public class PgController {

    @Autowired
    private PgService pgService;

    @Autowired
    private com.parent.config.JwtService jwtService;

    // ---------------------------------------------------------
    // OWNER → all PGs
    // MANAGER → only allowed PGs
    // ---------------------------------------------------------
    @GetMapping
    public List<PgResponse> getAllPgs(HttpServletRequest request) {

        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        Long managerId = jwtService.extractManagerIdFromRequest(request);

        if (ownerId != null) {
            return pgService.getAllPgs();
        }

        if (managerId != null) {
            Set<Long> allowed = jwtService.extractAllowedPgIdsFromRequest(request);
            return pgService.getPgsByIds(allowed);
        }

        return List.of();
    }


    // ---------------------------------------------------------
    // OWNER → only own PG
    // MANAGER → only PGs assigned
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public PgResponse getPgById(@PathVariable Long id, HttpServletRequest request) {
        return pgService.getPgById(id, request);
    }


    // ---------------------------------------------------------
    // OWNER ONLY — Create PG
    // ---------------------------------------------------------
    @PostMapping
    public PgResponse createPg(@RequestBody PgRequest pgRequest) {
        return pgService.createPg(pgRequest);
    }


    // ---------------------------------------------------------
    // OWNER ONLY — Update PG
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public PgResponse updatePg(@PathVariable Long id, @RequestBody PgRequest pgRequest) {
        return pgService.updatePg(id, pgRequest);
    }


    @DeleteMapping("/{id}")
    public String deletePg(@PathVariable Long id) {
        pgService.deletePg(id);
        return "PG deleted successfully!";
    }
}
