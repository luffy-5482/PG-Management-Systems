package com.parent.tenant.controller;

import com.parent.tenant.dto.SupportCenterConfigDto;
import com.parent.tenant.dto.SupportCenterConfigDto.EmergencyContactDto;
import com.parent.tenant.dto.SupportCenterConfigDto.FaqItemDto;
import com.parent.tenant.dto.SupportCenterConfigDto.SupportChannelDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/support")
public class TenantSupportConfigController {

    @GetMapping("/config")
    public ResponseEntity<SupportCenterConfigDto> getSupportConfig() {

        SupportCenterConfigDto dto = new SupportCenterConfigDto();

        // ---------- 1. Support channels ----------
        SupportChannelDto call = new SupportChannelDto();
        call.setType("CALL");
        call.setTitle("Call Support");
        call.setSubtitle("Speak directly with our support team");
        call.setValue("+91 98765 43210");
        call.setNote("Available 24/7");

        SupportChannelDto whatsapp = new SupportChannelDto();
        whatsapp.setType("WHATSAPP");
        whatsapp.setTitle("WhatsApp");
        whatsapp.setSubtitle("Quick chat support via WhatsApp");
        whatsapp.setValue("+91 98765 43210");
        whatsapp.setNote("Response within 15 mins");

        SupportChannelDto email = new SupportChannelDto();
        email.setType("EMAIL");
        email.setTitle("Email Support");
        email.setSubtitle("Send us detailed queries via email");
        email.setValue("support@pgmanager.com");
        email.setNote("Response within 2 hours");

        dto.setChannels(List.of(call, whatsapp, email));

        // ---------- 2. FAQs ----------
        FaqItemDto f1 = new FaqItemDto();
        f1.setQuestion("How do I pay my monthly rent online?");
        f1.setAnswer("Go to Payments > Pay Rent and complete the payment using the available methods.");

        FaqItemDto f2 = new FaqItemDto();
        f2.setQuestion("How can I raise a maintenance request?");
        f2.setAnswer("Use the Support page and submit a support ticket under 'Maintenance Issue' category.");

        FaqItemDto f3 = new FaqItemDto();
        f3.setQuestion("Can I change my room or request a room transfer?");
        f3.setAnswer("Please contact the PG manager. Room transfers are subject to availability.");

        dto.setFaqs(List.of(f1, f2, f3));

        // ---------- 3. Emergency contacts ----------
        EmergencyContactDto manager = new EmergencyContactDto();
        manager.setRole("PG Manager");
        manager.setName("Mr. Rajesh Kumar");
        manager.setPhone("+91 98765 43210");
        manager.setEmail("manager@sunrisepg.com");
        manager.setAvailability("Available 24/7");

        EmergencyContactDto security = new EmergencyContactDto();
        security.setRole("Security");
        security.setName("Security Desk");
        security.setPhone("+91 98765 43211");
        security.setEmail(null);
        security.setAvailability("Available 24/7");

        dto.setEmergencyContacts(List.of(manager, security));

        return ResponseEntity.ok(dto);
    }
}
