package com.parent.tenant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parent.tenant.enums.SupportCategory;
import com.parent.tenant.model.TenantSupportTicket;
import com.parent.tenant.repository.TenantSupportTicketRepository;

@Service
public class TenantSupportService {

    private final TenantSupportTicketRepository repo;
    private final TenantActivityLogService logService;

    public TenantSupportService(
            TenantSupportTicketRepository repo,
            TenantActivityLogService logService
    ) {
        this.repo = repo;
        this.logService = logService;
    }

    // ---------------------------------
    // CREATE TICKET
    // ---------------------------------
    @Transactional
    public TenantSupportTicket createTicket(
            Long tenantId,
            Long pgId,
            SupportCategory category,
            String subject,
            String description
    ) {

        TenantSupportTicket ticket = new TenantSupportTicket();
        ticket.setTenantId(tenantId);
        ticket.setPgId(pgId);
        ticket.setCategory(category);
        ticket.setSubject(subject);
        ticket.setDescription(description);

        ticket = repo.save(ticket);

        logService.log(
                tenantId,
                "SUPPORT_CREATED",
                "Support ticket created: " + subject
        );

        return ticket;
    }

    // ---------------------------------
    // VIEW MY TICKETS
    // ---------------------------------
    public List<TenantSupportTicket> myTickets(Long tenantId) {
        return repo.findByTenantId(tenantId);
    }
}
