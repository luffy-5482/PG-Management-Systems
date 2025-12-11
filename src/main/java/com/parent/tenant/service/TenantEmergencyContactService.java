package com.parent.tenant.service;

import com.parent.tenant.dto.EmergencyContactDto;

import java.util.List;

public interface TenantEmergencyContactService {

    List<EmergencyContactDto> getContacts(Long tenantId);

    EmergencyContactDto addContact(Long tenantId, EmergencyContactDto dto);

    EmergencyContactDto updateContact(Long tenantId, Long contactId, EmergencyContactDto dto);

    void deleteContact(Long tenantId, Long contactId);
}
