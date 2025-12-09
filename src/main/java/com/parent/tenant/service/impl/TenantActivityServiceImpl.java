package com.parent.tenant.service.impl;

import com.parent.payment.model.TenantPayment;
import com.parent.payment.model.TenantSupportTicket;
import com.parent.payment.repository.TenantPaymentRepository;
import com.parent.payment.repository.TenantSupportTicketRepository;
import com.parent.tenant.dto.TenantActivityDto;
import com.parent.tenant.service.TenantActivityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TenantActivityServiceImpl implements TenantActivityService {

    private final TenantPaymentRepository paymentRepository;
    private final TenantSupportTicketRepository ticketRepository;

    public TenantActivityServiceImpl(TenantPaymentRepository paymentRepository,
                                     TenantSupportTicketRepository ticketRepository) {
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<TenantActivityDto> getActivityForTenant(Long tenantId) {

        List<TenantActivityDto> items = new ArrayList<>();

        // 1) Payments
        List<TenantPayment> payments = paymentRepository.findByTenant_IdOrderByPaidAtDesc(tenantId);
        for (TenantPayment p : payments) {
            TenantActivityDto dto = new TenantActivityDto();
            dto.setType("PAYMENT");
            dto.setTitle("Rent paid");
            dto.setSubtitle("₹" + p.getAmount().intValue());
            dto.setAmount(p.getAmount());
            dto.setStatus(p.getStatus());
            dto.setIcon("payment");
            dto.setCreatedAt(p.getPaidAt());
            items.add(dto);
        }

        // 2) Tickets
        List<TenantSupportTicket> tickets = ticketRepository.findByTenant_IdOrderByCreatedAtDesc(tenantId);
        for (TenantSupportTicket t : tickets) {
            TenantActivityDto dto = new TenantActivityDto();
            dto.setType("TICKET");
            dto.setTitle(t.getSubject());
            dto.setSubtitle(t.getCategory() + " • " + t.getPriority());
            dto.setAmount(null);
            dto.setStatus(t.getStatus());
            dto.setIcon("ticket");
            dto.setCreatedAt(t.getCreatedAt());
            items.add(dto);
        }

        // newest first
        items.sort(Comparator.comparing(TenantActivityDto::getCreatedAt).reversed());

        return items;
    }

    @Override
    public void recordActivity(Long tenantId, String type, String title, String description) {
        // no-op for now: activity is computed from payments + tickets
    }
}
