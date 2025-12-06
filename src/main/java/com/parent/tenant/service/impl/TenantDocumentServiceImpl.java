package com.parent.tenant.service.impl;

import com.parent.payment.model.Tenant;
import com.parent.payment.repository.TenantRepository;
import com.parent.tenant.dto.TenantDocumentDto;
import com.parent.tenant.model.TenantDocument;
import com.parent.tenant.repository.TenantDocumentRepository;
import com.parent.tenant.service.TenantDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantDocumentServiceImpl implements TenantDocumentService {

    private final TenantRepository tenantRepository;
    private final TenantDocumentRepository documentRepository;

    // Folder where files will be stored: uploads/tenant-documents/tenant-{id}/
    private final Path rootLocation = Paths.get("uploads/tenant-documents");

    public TenantDocumentServiceImpl(TenantRepository tenantRepository,
                                     TenantDocumentRepository documentRepository) {
        this.tenantRepository = tenantRepository;
        this.documentRepository = documentRepository;

        // Ensure base upload directory exists
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder", e);
        }
    }

    @Override
    public List<TenantDocumentDto> getDocuments(Long tenantId) {
        return documentRepository.findByTenant_Id(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TenantDocumentDto addDocument(Long tenantId, TenantDocumentDto dto) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        TenantDocument doc = new TenantDocument();
        doc.setTenant(tenant);
        doc.setName(dto.getName());
        doc.setType(dto.getType());
        doc.setFileUrl(dto.getFileUrl()); // assuming frontend gives you a URL
        doc.setUploadedAt(LocalDateTime.now());

        TenantDocument saved = documentRepository.save(doc);
        return toDto(saved);
    }

    @Override
    public void deleteDocument(Long tenantId, Long documentId) {
        TenantDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        // safety: prevent deleting other tenant's docs
        if (!doc.getTenant().getId().equals(tenantId)) {
            throw new RuntimeException("Document does not belong to this tenant");
        }

        // Try to delete file from disk if fileUrl is a local path
        if (doc.getFileUrl() != null && !doc.getFileUrl().isBlank()) {
            try {
                Path filePath = Paths.get(doc.getFileUrl());
                Files.deleteIfExists(filePath);
            } catch (Exception ignored) {
                // don't break if file is missing – just log in real app
            }
        }

        documentRepository.delete(doc);
    }

    // 🔹 NEW: upload real file + save metadata
    @Override
    public TenantDocumentDto uploadDocument(Long tenantId, String type, MultipartFile file) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        if (file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty");
        }

        try {
            // tenant-specific folder: uploads/tenant-documents/tenant-1/
            Path tenantDir = rootLocation.resolve("tenant-" + tenantId);
            Files.createDirectories(tenantDir);

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                originalFilename = "document";
            }

            // simple unique filename: timestamp_originalName
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
            Path targetLocation = tenantDir.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Save metadata in DB
            TenantDocument doc = new TenantDocument();
            doc.setTenant(tenant);
            doc.setName(originalFilename);            // human-readable name
            doc.setType(type);
            doc.setFileUrl(targetLocation.toString()); // full path on disk
            doc.setUploadedAt(LocalDateTime.now());

            TenantDocument saved = documentRepository.save(doc);
            return toDto(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    // 🔹 NEW: load file for download
    @Override
    public Resource loadDocumentAsResource(Long tenantId, Long documentId) {
        TenantDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        if (!doc.getTenant().getId().equals(tenantId)) {
            throw new RuntimeException("Document does not belong to this tenant");
        }

        try {
            Path filePath = Paths.get(doc.getFileUrl());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("File not found", e);
        }
    }

    // -------- mapping helper --------

    private TenantDocumentDto toDto(TenantDocument doc) {
        TenantDocumentDto dto = new TenantDocumentDto();
        dto.setId(doc.getId());
        dto.setName(doc.getName());
        dto.setType(doc.getType());
        dto.setFileUrl(doc.getFileUrl());
        dto.setUploadedAt(doc.getUploadedAt());
        // if your DTO has fileName, you can set it here as well:
        // dto.setFileName(doc.getName());
        return dto;
    }
}
