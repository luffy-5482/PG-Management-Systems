package com.parent.tenant.controller;

import com.parent.tenant.dto.EmergencyContactDto;
import com.parent.tenant.service.TenantEmergencyContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/{tenantId}/settings/emergency")
public class TenantEmergencyContactController {

    private final TenantEmergencyContactService service;

    public TenantEmergencyContactController(TenantEmergencyContactService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmergencyContactDto> getContacts(@PathVariable Long tenantId) {
        return service.getContacts(tenantId);
    }

    @PostMapping
    public EmergencyContactDto addContact(
            @PathVariable Long tenantId,
            @RequestBody EmergencyContactDto dto
    ) {
        return service.addContact(tenantId, dto);
    }

    @PutMapping("/{contactId}")
    public EmergencyContactDto updateContact(
            @PathVariable Long tenantId,
            @PathVariable Long contactId,
            @RequestBody EmergencyContactDto dto
    ) {
        return service.updateContact(tenantId, contactId, dto);
    }

    @DeleteMapping("/{contactId}")
    public void deleteContact(
            @PathVariable Long tenantId,
            @PathVariable Long contactId
    ) {
        service.deleteContact(tenantId, contactId);
    }
}
