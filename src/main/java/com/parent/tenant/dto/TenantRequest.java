package com.parent.tenant.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TenantRequest {

    private Long id;

    @NotBlank(message = "name required")
    private String name;

    @NotBlank @Email
    private String email;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "phone must be 10-15 digits")
    private String phone;

    @NotNull(message = "roomId required")
    private Long roomId;

    private String password;

    // NEW FIELDS
    private Double rent;
    private Double due;
    private LocalDate dueDate;
    private String status;
    private String avatar;

    public TenantRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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
}
