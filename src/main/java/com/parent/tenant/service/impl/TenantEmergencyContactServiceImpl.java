package com.parent.tenant.service.impl;

import com.parent.tenant.dto.EmergencyContactDto;
import com.parent.tenant.model.EmergencyContact;
import com.parent.tenant.repository.EmergencyContactRepository;
import com.parent.tenant.service.TenantEmergencyContactService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantEmergencyContactServiceImpl implements TenantEmergencyContactService {

    private final EmergencyContactRepository contactRepo;

    public TenantEmergencyContactServiceImpl(EmergencyContactRepository contactRepo) {
        this.contactRepo = contactRepo;
    }

    @Override
    public List<EmergencyContactDto> getContacts(Long tenantId) {
        return contactRepo.findByTenantId(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmergencyContactDto addContact(Long tenantId, EmergencyContactDto dto) {
        EmergencyContact ec = new EmergencyContact();
        ec.setTenantId(tenantId);
        ec.setName(dto.getName());
        ec.setRelationship(dto.getRelationship());
        ec.setPhone(dto.getPhone());
        ec.setEmail(dto.getEmail());

        EmergencyContact saved = contactRepo.save(ec);
        return toDto(saved);
    }

    @Override
    public EmergencyContactDto updateContact(Long tenantId, Long contactId, EmergencyContactDto dto) {
        EmergencyContact ec = contactRepo.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        if (!ec.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Unauthorized update");
        }

        ec.setName(dto.getName());
        ec.setRelationship(dto.getRelationship());
        ec.setPhone(dto.getPhone());
        ec.setEmail(dto.getEmail());

        return toDto(contactRepo.save(ec));
    }

    @Override
    public void deleteContact(Long tenantId, Long contactId) {
        EmergencyContact ec = contactRepo.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        if (!ec.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Unauthorized delete");
        }

        contactRepo.delete(ec);
    }

    // ------------------ Mapping ------------------

    private EmergencyContactDto toDto(EmergencyContact ec) {
        EmergencyContactDto dto = new EmergencyContactDto();
        dto.setId(ec.getId());
        dto.setName(ec.getName());
        dto.setRelationship(ec.getRelationship());
        dto.setPhone(ec.getPhone());
        dto.setEmail(ec.getEmail());
        return dto;
    }
}
