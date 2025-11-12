package com.parent.owner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.owner.dto.OwnerRequest;
import com.parent.owner.dto.OwnerResponse;
import com.parent.owner.service.OwnerService;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "*")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public List<OwnerResponse> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public OwnerResponse getOwnerById(@PathVariable Long id) {
        return ownerService.getOwnerById(id);
    }

    @PostMapping
    public OwnerResponse createOwner(@RequestBody OwnerRequest request) {
        return ownerService.createOwner(request);
    }

    @PutMapping("/{id}")
    public OwnerResponse updateOwner(@PathVariable Long id, @RequestBody OwnerRequest request) {
        return ownerService.updateOwner(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return "Owner deleted successfully!";
    }
}
