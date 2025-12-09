package com.parent.tenant.service.impl;

import com.parent.payment.model.Tenant;
import com.parent.payment.model.TenantSupportTicket;
import com.parent.payment.repository.TenantRepository;
import com.parent.payment.repository.TenantSupportTicketRepository;
import com.parent.tenant.dto.TenantSupportTicketDto;
import com.parent.tenant.service.TenantSupportTicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantSupportTicketServiceImpl implements TenantSupportTicketService {

    private final TenantRepository tenantRepository;
    private final TenantSupportTicketRepository ticketRepository;

    public TenantSupportTicketServiceImpl(TenantRepository tenantRepository,
                                          TenantSupportTicketRepository ticketRepository) {
        this.tenantRepository = tenantRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<TenantSupportTicketDto> getTickets(Long tenantId) {
        return ticketRepository.findByTenant_IdOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TenantSupportTicketDto createTicket(Long tenantId, TenantSupportTicketDto request) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        TenantSupportTicket ticket = new TenantSupportTicket();
        ticket.setTenant(tenant);
        ticket.setSubject(request.getSubject());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(request.getPriority());
        ticket.setDescription(request.getDescription());
        ticket.setStatus("OPEN"); // default

        TenantSupportTicket saved = ticketRepository.save(ticket);
        return toDto(saved);
    }

    @Override
    public TenantSupportTicketDto updateStatus(Long tenantId, Long ticketId, String status) {

        TenantSupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

        // safety: ensure ticket belongs to this tenant
        if (ticket.getTenant() == null || !ticket.getTenant().getId().equals(tenantId)) {
            throw new RuntimeException("Ticket does not belong to tenant id: " + tenantId);
        }

        ticket.setStatus(status);
        TenantSupportTicket saved = ticketRepository.save(ticket);
        return toDto(saved);
    }

    // ---------------- helper mapper ----------------

    private TenantSupportTicketDto toDto(TenantSupportTicket t) {
        TenantSupportTicketDto dto = new TenantSupportTicketDto();
        dto.setId(t.getId());
        if (t.getTenant() != null) {
            dto.setTenantId(t.getTenant().getId());
        }
        dto.setSubject(t.getSubject());
        dto.setCategory(t.getCategory());
        dto.setPriority(t.getPriority());
        dto.setDescription(t.getDescription());
        dto.setStatus(t.getStatus());
        dto.setCreatedAt(t.getCreatedAt());
        return dto;
    }
}
