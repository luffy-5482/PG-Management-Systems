package com.parent.tenant.service;

import com.parent.tenant.dto.TenantSupportTicketDto;

import java.util.List;

public interface TenantSupportTicketService {

    // list all tickets for one tenant
    List<TenantSupportTicketDto> getTickets(Long tenantId);

    // create new ticket
    TenantSupportTicketDto createTicket(Long tenantId, TenantSupportTicketDto request);

    // update just the status (OPEN -> IN_PROGRESS -> CLOSED)
    TenantSupportTicketDto updateStatus(Long tenantId, Long ticketId, String status);
}
