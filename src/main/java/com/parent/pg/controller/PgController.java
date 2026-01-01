package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.parent.manager.model.Manager;
import com.parent.manager.repository.ManagerRepository;
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
    private JwtService jwtService;
    @Autowired
    private ManagerRepository ManagerRepository;

    // ---------------------------------------------------------
    // OWNER → all PGs
    // MANAGER → only allowed PGs
    // ---------------------------------------------------------
    @GetMapping
    public List<PgResponse> getAllPgs(HttpServletRequest request) {
    	
        Long ownerId = jwtService.extractOwnerIdFromRequest(request);
        Long managerId = jwtService.extractManagerIdFromRequest(request);

        // OWNER → return all PGs
        if (ownerId != null) {
            return pgService.getAllPgs();
        }

        // MANAGER → return only allowed PGs FROM DATABASE (NOT TOKEN)
        if (managerId != null) {
            Manager m = ManagerRepository.findById(managerId)
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            return pgService.getPgsByIds(m.getAllowedPgIds());
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
