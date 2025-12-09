package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantSupportTicketDto;
import com.parent.tenant.service.TenantSupportTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/{tenantId}/tickets")
public class TenantSupportTicketController {

    private final TenantSupportTicketService ticketService;

    public TenantSupportTicketController(TenantSupportTicketService ticketService) {
        this.ticketService = ticketService;
    }

    // GET /api/tenant/1/tickets
    @GetMapping
    public ResponseEntity<List<TenantSupportTicketDto>> getTickets(@PathVariable Long tenantId) {
        return ResponseEntity.ok(ticketService.getTickets(tenantId));
    }

    // POST /api/tenant/1/tickets
    @PostMapping
    public ResponseEntity<TenantSupportTicketDto> createTicket(
            @PathVariable Long tenantId,
            @RequestBody TenantSupportTicketDto body
    ) {
        TenantSupportTicketDto created = ticketService.createTicket(tenantId, body);
        return ResponseEntity.ok(created);
    }

    // PATCH /api/tenant/1/tickets/{ticketId}/status
    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<TenantSupportTicketDto> updateStatus(
            @PathVariable Long tenantId,
            @PathVariable Long ticketId,
            @RequestBody StatusUpdateRequest body
    ) {
        TenantSupportTicketDto updated = ticketService.updateStatus(tenantId, ticketId, body.getStatus());
        return ResponseEntity.ok(updated);
    }

    // small inner class for JSON: { "status": "CLOSED" }
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
