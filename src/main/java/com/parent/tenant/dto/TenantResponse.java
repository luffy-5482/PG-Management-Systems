package com.parent.tenant.dto;

import java.time.Instant;
import java.time.LocalDate;

public class TenantResponse {

    private Long id;
    private String name;

    private Long roomId;
    private String room;        // roomNumber
    private String contact;     // phone

    private String email;

    // NEW FIELDS
    private Double rent;
    private Double due;
    private LocalDate dueDate;

    private String status;
    private String avatar;

    private Instant joinDate;

    // only on create
    private String password;

    public TenantResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getRent() { return rent; }
    public void setRent(Double rent) { this.rent = rent; }

    public Double getDue() { return due; }
    public void setDue(Double due) { this.due = due; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Instant getJoinDate() { return joinDate; }
    public void setJoinDate(Instant joinDate) { this.joinDate = joinDate; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
