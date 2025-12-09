package com.parent.tenant.dto;

import java.util.List;

public class SupportCenterConfigDto {

    // "Need Help?" cards
    private List<SupportChannelDto> channels;

    // FAQ items
    private List<FaqItemDto> faqs;

    // Emergency contacts cards
    private List<EmergencyContactDto> emergencyContacts;

    // ------------ getters & setters ------------

    public List<SupportChannelDto> getChannels() {
        return channels;
    }

    public void setChannels(List<SupportChannelDto> channels) {
        this.channels = channels;
    }

    public List<FaqItemDto> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FaqItemDto> faqs) {
        this.faqs = faqs;
    }

    public List<EmergencyContactDto> getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(List<EmergencyContactDto> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    // ---------------------------------------------------
    // nested simple DTOs
    // ---------------------------------------------------

    public static class SupportChannelDto {
        private String type;        // CALL / WHATSAPP / EMAIL
        private String title;       // "Call Support"
        private String subtitle;    // "Speak directly with..."
        private String value;       // phone number or email
        private String note;        // "Available 24/7" etc.

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getSubtitle() { return subtitle; }
        public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
    }

    public static class FaqItemDto {
        private String question;
        private String answer;

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }

    public static class EmergencyContactDto {
        private String role;        // "PG Manager"
        private String name;        // "Mr. Rajesh Kumar"
        private String phone;       // "+91 98765 43210"
        private String email;       // "manager@..."
        private String availability; // "Available 24/7"

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getAvailability() { return availability; }
        public void setAvailability(String availability) { this.availability = availability; }
    }
}
