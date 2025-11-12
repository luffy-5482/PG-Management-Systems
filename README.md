# PG-Management-Systems-Backend (Spring Boot)
# 🧩 Backend Modification Report — Contact Person Feature
 
**Branch:** Backend  
**Project:** PG Management System (Spring Boot + PostgreSQL)   

---

## 🏗️ Overview

This document describes the modifications made to the **PG Management System backend** to implement the **Contact Person feature**.  

The goal:  
> When a PG (Paying Guest) property is created, updated, or fetched, its associated **Contact Person details** should also be stored and displayed.

---

## ⚙️ Summary of Updates

| File Modified / Added | Location | Description |
|------------------------|-----------|--------------|
| **`PgEntity.java`** | `src/main/java/com/parent/pg/model/` | Added `@OneToOne` mapping with new `ContactPerson` entity |
| **`ContactPerson.java`** | `src/main/java/com/parent/pg/model/` | 🆕 New entity representing the contact person details |
| **`ContactPersonRepository.java`** | `src/main/java/com/parent/pg/repository/` | 🆕 New repository interface extending `JpaRepository` |
| **`PgRequest.java`** | `src/main/java/com/parent/pg/dto/` | Added nested object `ContactPersonRequest contactPerson` |
| **`PgResponse.java`** | `src/main/java/com/parent/pg/dto/` | Added `ContactPersonResponse contactPerson` field for response mapping |
| **`PgServiceImpl.java`** | `src/main/java/com/parent/pg/service/` | Updated `createPg()` and `updatePg()` methods to handle Contact Person creation and mapping |
| **`README.md`** | root folder | Updated documentation for API usage and feature summary |

---

## 🧱 Database Changes

A new table was created: **`contact_persons`**

| Column | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL | Primary key |
| name | VARCHAR | Contact person name |
| number | VARCHAR | Contact number |
| role | VARCHAR | Designation (e.g., Manager, Staff) |
| pg_id | BIGINT (FK) | Foreign key referencing `pgs.id` |

---

## 🧩 Detailed Code Modifications

### 1️⃣ **PgEntity.java**
Added one-to-one mapping to ContactPerson:

```java
@OneToOne(mappedBy = "pg", cascade = CascadeType.ALL)
@JsonManagedReference
private ContactPerson contactPerson;

public ContactPerson getContactPerson() { return contactPerson; }
public void setContactPerson(ContactPerson contactPerson) { this.contactPerson = contactPerson; }
```

## 🧩 Contact Person Feature Implementation

---

### 2️⃣ New Entity — **ContactPerson.java**

```java
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
    @JsonBackReference
    private PgEntity pg;

    // getters and setters
} 
```

### 3️⃣ **PgRequest.java**

Added nested DTO for incoming contact details:

private ContactPersonRequest contactPerson;


Example JSON:
```java
"contactPerson": {
  "name": "Anjali Mehta",
  "number": "9876543210",
  "role": "Manager"
}
```

### 4️⃣ **PgResponse.java**

Added nested DTO for outgoing contact details:
```java
private ContactPersonResponse contactPerson;
```

### 5️⃣ **PgServiceImpl.java**

Updated createPg() and updatePg() methods to handle Contact Person save logic.

Inside createPg(PgRequest request) after saving PG:
```java
if (request.getContactPerson() != null) {
    ContactPerson contact = new ContactPerson();
    contact.setName(request.getContactPerson().getName());
    contact.setNumber(request.getContactPerson().getNumber());
    contact.setRole(request.getContactPerson().getRole());
    contact.setPg(saved);
    contactPersonRepository.save(contact);
    saved.setContactPerson(contact);
}
```

Inside toPgResponse(PgEntity pg):
```java
if (pg.getContactPerson() != null) {
    ContactPersonResponse c = new ContactPersonResponse();
    c.setId(pg.getContactPerson().getId());
    c.setName(pg.getContactPerson().getName());
    c.setNumber(pg.getContactPerson().getNumber());
    c.setRole(pg.getContactPerson().getRole());
    response.setContactPerson(c);
}
```
