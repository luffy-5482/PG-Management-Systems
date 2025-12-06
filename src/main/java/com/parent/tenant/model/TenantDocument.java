package com.parent.tenant.model;

import com.parent.payment.model.Tenant;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_documents")
public class TenantDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which tenant this doc belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // Display name in UI (e.g., "Aadhaar Card", "Rent Agreement")
    @Column(nullable = false)
    private String name;

    // Type: "ID_PROOF", "ADDRESS_PROOF", "AGREEMENT", etc.
    @Column(nullable = false)
    private String type;

    // Where the file is stored (S3 URL, backend URL, etc.)
    @Column(nullable = false)
    private String fileUrl;

    private LocalDateTime uploadedAt;

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
