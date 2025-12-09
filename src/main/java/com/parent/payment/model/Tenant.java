package com.parent.payment.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic info
    private String name;
    private String email;
    private String contact;

    // Profile fields
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String gender;
    private String occupation;

    // -------- Tenant Management fields (for tenant list page) --------
    // e.g. "101"
    private String room;

    // e.g. 4500
    private Integer rent;

    // e.g. 1500
    private Integer due;

    // e.g. "2025-01-15"
    @Column(name = "join_date")
    private LocalDate joinDate;

    // e.g. "2025-02-10"
    @Column(name = "due_date")
    private LocalDate dueDate;

    // e.g. "rent_due", "paid", etc.
    private String status;

    // URL or path to avatar image (nullable)
    private String avatar;

    // -------- Subscription fields (monthly blocking logic) --------
    // monthly subscription / rent amount
    private Integer monthlySubscriptionAmount;

    // when the next payment is due
    private LocalDate nextDueDate;

    // whether tenant is blocked from using module
    private Boolean subscriptionBlocked = Boolean.FALSE;

    // -------- Constructors --------

    public Tenant() {
    }

    public Tenant(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // basic info

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

    // profile fields

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

    // tenant management fields

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getRent() {
        return rent;
    }

    public void setRent(Integer rent) {
        this.rent = rent;
    }

    public Integer getDue() {
        return due;
    }

    public void setDue(Integer due) {
        this.due = due;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // subscription fields

    public Integer getMonthlySubscriptionAmount() {
        return monthlySubscriptionAmount;
    }

    public void setMonthlySubscriptionAmount(Integer monthlySubscriptionAmount) {
        this.monthlySubscriptionAmount = monthlySubscriptionAmount;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public Boolean getSubscriptionBlocked() {
        return subscriptionBlocked;
    }

    public void setSubscriptionBlocked(Boolean subscriptionBlocked) {
        this.subscriptionBlocked = subscriptionBlocked;
    }
}
