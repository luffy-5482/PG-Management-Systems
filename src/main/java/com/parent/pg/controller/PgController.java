package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.pg.model.PgEntity;
import com.parent.pg.service.PgService;

@RestController
@RequestMapping("/api/pgs")
public class PgController {

    @Autowired
    private PgService pgService;

    @GetMapping
    public List<PgEntity> getAllPgs() {
        return pgService.getAllPgs();
    }

    @GetMapping("/{id}")
    public PgEntity getPgById(@PathVariable Long id) {
        return pgService.getPgById(id);
    }
    @PostMapping
    public PgEntity createPg(@RequestBody PgEntity pg) {
        return pgService.createPg(pg);
    }
    @PutMapping("/{id}")
    public PgEntity updatePg(@PathVariable Long id, @RequestBody PgEntity pg) {
        return pgService.updatePg(id, pg);
    }

    @DeleteMapping("/{id}")
    public String deletePg(@PathVariable Long id) {
        pgService.deletePg(id);
        return "PG deleted successfully!";
    }
}
