package com.parent.pg.dto;

public class ContactPersonRequest {
    private String name;
    private String number;
    private String role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
