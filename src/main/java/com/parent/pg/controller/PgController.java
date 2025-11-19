package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.PgRequest;
import com.parent.pg.dto.PgResponse;
import com.parent.pg.service.PgService;

@RestController
@RequestMapping("/api/pgs")
@CrossOrigin(origins = "*")
public class PgController {

    @Autowired
    private PgService pgService;

   
    @GetMapping
    public List<PgResponse> getAllPgs() {
        return pgService.getAllPgs();  // already owner-secured
    }
    
    @GetMapping("/{id}")
    public PgResponse getPgById(@PathVariable Long id) {
        return pgService.getPgById(id);  // service enforces ownership
    }

    // ---------------------------------------------------------
    // 🔥 Create a PG — automatically assigned to logged-in owner
    // ---------------------------------------------------------
    @PostMapping
    public PgResponse createPg(@RequestBody PgRequest pgRequest) {
        return pgService.createPg(pgRequest);
    }

    // ---------------------------------------------------------
    // 🔥 Update PG — only if this PG belongs to logged-in owner
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
