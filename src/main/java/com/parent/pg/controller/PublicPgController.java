package com.parent.pg.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.PgResponse;
import com.parent.pg.service.PgService;

@RestController
@RequestMapping("/api/public/pgs")
@CrossOrigin(origins = "*")
public class PublicPgController {

    private final PgService pgService;

    public PublicPgController(PgService pgService) {
        this.pgService = pgService;
    }

    // -------------------------------------------------
    // PUBLIC → LIST ALL PGs (BROWSE)
    // -------------------------------------------------
    @GetMapping
    public List<PgResponse> browseAll() {
        return pgService.getAllPgsPublic();
    }

    // -------------------------------------------------
    // PUBLIC → VIEW SINGLE PG
    // -------------------------------------------------
    @GetMapping("/{id}")
    public PgResponse view(@PathVariable Long id) {
        return pgService.getPgPublicById(id);
    }
}
