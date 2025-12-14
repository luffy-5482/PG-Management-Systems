package com.parent.tenant.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

import com.parent.pg.model.RoomEntity;

@Entity
@Table(name = "tenants")
public class TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    private Long ownerId;
    private Long pgId;
    private Long floorId;

    private boolean active = true;

    private Instant joinedAt = Instant.now();

    // NEW FIELDS
    private Double rent;
    private Double due;
    private LocalDate dueDate;
    private String rentStatus;

    private String avatar; // optional image URL

    private String createdBy;
    private String updatedBy;

    public TenantEntity() {}

    // getters + setters below
    // ---------- BASIC ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public RoomEntity getRoom() { return room; }
    public void setRoom(RoomEntity room) { this.room = room; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public Long getPgId() { return pgId; }
    public void setPgId(Long pgId) { this.pgId = pgId; }

    public Long getFloorId() { return floorId; }
    public void setFloorId(Long floorId) { this.floorId = floorId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    // ---------- NEW FIELDS ----------
    public Double getRent() { return rent; }
    public void setRent(Double rent) { this.rent = rent; }

    public Double getDue() { return due; }
    public void setDue(Double due) { this.due = due; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getRentStatus() { return rentStatus; }
    public void setRentStatus(String rentStatus) { this.rentStatus = rentStatus; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
