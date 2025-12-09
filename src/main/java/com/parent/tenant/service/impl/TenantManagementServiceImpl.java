package com.parent.tenant.service.impl;

import com.parent.payment.model.Tenant;
import com.parent.payment.repository.TenantRepository;
import com.parent.tenant.dto.TenantManagementDto;
import com.parent.tenant.service.TenantManagementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantManagementServiceImpl implements TenantManagementService {

    private final TenantRepository tenantRepository;

    public TenantManagementServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public List<TenantManagementDto> getAllTenants() {
        return tenantRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TenantManagementDto> searchTenants(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllTenants();
        }

        String q = "%" + query.toLowerCase() + "%";

        // simplest: fetch all & filter in memory
        return tenantRepository.findAll()
                .stream()
                .filter(t ->
                        (t.getName() != null && t.getName().toLowerCase().contains(query.toLowerCase())) ||
                        (t.getEmail() != null && t.getEmail().toLowerCase().contains(query.toLowerCase())) ||
                        (t.getContact() != null && t.getContact().toLowerCase().contains(query.toLowerCase())) ||
                        (t.getRoom() != null && t.getRoom().toLowerCase().contains(query.toLowerCase()))
                )
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TenantManagementDto createTenant(TenantManagementDto request) {
        Tenant tenant = new Tenant();
        applyFromDto(tenant, request);
        Tenant saved = tenantRepository.save(tenant);
        return toDto(saved);
    }

    @Override
    public TenantManagementDto updateTenant(Long id, TenantManagementDto request) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));

        applyFromDto(tenant, request);
        Tenant saved = tenantRepository.save(tenant);
        return toDto(saved);
    }

    @Override
    public void deleteTenant(Long id) {
        if (!tenantRepository.existsById(id)) {
            throw new RuntimeException("Tenant not found with id: " + id);
        }
        tenantRepository.deleteById(id);
    }

    @Override
    public TenantManagementDto getTenantById(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
        return toDto(tenant);
    }

    // ---------- mapping helpers ----------

    private TenantManagementDto toDto(Tenant tenant) {
        TenantManagementDto dto = new TenantManagementDto();
        dto.setId(tenant.getId());
        dto.setName(tenant.getName());
        dto.setRoom(tenant.getRoom());
        dto.setContact(tenant.getContact());
        dto.setEmail(tenant.getEmail());
        dto.setRent(tenant.getRent());
        dto.setDue(tenant.getDue());
        dto.setJoinDate(tenant.getJoinDate());
        dto.setDueDate(tenant.getDueDate());
        dto.setStatus(tenant.getStatus());
        dto.setAvatar(tenant.getAvatar());
        return dto;
    }

    private void applyFromDto(Tenant tenant, TenantManagementDto dto) {
        if (dto.getName() != null) tenant.setName(dto.getName());
        if (dto.getRoom() != null) tenant.setRoom(dto.getRoom());
        if (dto.getContact() != null) tenant.setContact(dto.getContact());
        if (dto.getEmail() != null) tenant.setEmail(dto.getEmail());
        if (dto.getRent() != null) tenant.setRent(dto.getRent());
        if (dto.getDue() != null) tenant.setDue(dto.getDue());
        if (dto.getJoinDate() != null) tenant.setJoinDate(dto.getJoinDate());
        if (dto.getDueDate() != null) tenant.setDueDate(dto.getDueDate());
        if (dto.getStatus() != null) tenant.setStatus(dto.getStatus());
        if (dto.getAvatar() != null) tenant.setAvatar(dto.getAvatar());
    }
}
