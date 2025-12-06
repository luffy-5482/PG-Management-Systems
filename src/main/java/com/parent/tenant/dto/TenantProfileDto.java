package com.parent.tenant.dto;

import java.time.LocalDate;

public class TenantProfileDto {

    private Long id;
    private String name;
    private String email;
    private String contact;

    // New basic fields
    private LocalDate dateOfBirth;
    private String gender;
    private String occupation;

    // 🔥 NEW PART ADDED
    private TenantRoomDetailsDto roomDetails;

    // ----- Getters & Setters -----
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public TenantRoomDetailsDto getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(TenantRoomDetailsDto roomDetails) {
        this.roomDetails = roomDetails;
    }
}
