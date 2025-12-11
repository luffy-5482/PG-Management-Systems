package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.ContactPersonRequest;
import com.parent.pg.dto.ContactPersonResponse;
import com.parent.pg.service.ContactPersonService;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactPersonController {

    @Autowired
    private ContactPersonService service;

    // Create
    @PostMapping
    public ContactPersonResponse create(@RequestBody ContactPersonRequest req) {
        return service.createContact(req);
    }

    // Update
    @PutMapping("/{id}")
    public ContactPersonResponse update(@PathVariable Long id, @RequestBody ContactPersonRequest req) {
        return service.updateContact(id, req);
    }

    // Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteContact(id);
        return "Contact deleted successfully!";
    }

    // Get Contacts by PG
    @GetMapping("/pg/{pgId}")
    public List<ContactPersonResponse> getByPg(@PathVariable Long pgId) {
        return service.getContactsByPg(pgId);
    }
}
