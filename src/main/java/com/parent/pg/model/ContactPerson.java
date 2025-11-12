package com.parent.pg.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "contact_persons")
public class ContactPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String number;
    private String role;

    @OneToOne
    @JoinColumn(name = "pg_id")
    @JsonBackReference(value = "pg-contact")
    private PgEntity pg;

    // Constructors
    public ContactPerson() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public PgEntity getPg() { return pg; }
    public void setPg(PgEntity pg) { this.pg = pg; }
}
