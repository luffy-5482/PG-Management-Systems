package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantDocumentDto;
import com.parent.tenant.service.TenantDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/tenant/{tenantId}/documents")
public class TenantDocumentController {

    private final TenantDocumentService documentService;

    public TenantDocumentController(TenantDocumentService documentService) {
        this.documentService = documentService;
    }

    // GET all documents for a tenant
    @GetMapping
    public ResponseEntity<List<TenantDocumentDto>> getDocuments(@PathVariable Long tenantId) {
        return ResponseEntity.ok(documentService.getDocuments(tenantId));
    }

    // ADD document metadata (if frontend already has URL)
    @PostMapping
    public ResponseEntity<TenantDocumentDto> addDocument(
            @PathVariable Long tenantId,
            @RequestBody TenantDocumentDto dto
    ) {
        TenantDocumentDto created = documentService.addDocument(tenantId, dto);
        return ResponseEntity.ok(created);
    }

    // 🔹 NEW: Upload file
    @PostMapping("/upload")
    public ResponseEntity<TenantDocumentDto> uploadDocument(
            @PathVariable Long tenantId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) {
        TenantDocumentDto created = documentService.uploadDocument(tenantId, type, file);
        return ResponseEntity.ok(created);
    }

    // 🔹 NEW: Download file
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long tenantId,
            @PathVariable Long documentId
    ) {
        TenantDocumentDto docMeta = documentService.getDocuments(tenantId).stream()
                .filter(d -> d.getId().equals(documentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Document not found"));

        Resource resource = documentService.loadDocumentAsResource(tenantId, documentId);

        String encodedFileName = URLEncoder.encode(docMeta.getName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFileName
                )
                .body(resource);
    }

    

    // DELETE a document by id
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long tenantId,
            @PathVariable Long documentId
    ) {
        documentService.deleteDocument(tenantId, documentId);
        return ResponseEntity.noContent().build();
    }
}
