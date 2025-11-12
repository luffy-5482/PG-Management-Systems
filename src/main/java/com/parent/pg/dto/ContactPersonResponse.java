package com.parent.pg.dto;

public class ContactPersonResponse {
    private Long id;
    private String name;
    private String number;
    private String role;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
