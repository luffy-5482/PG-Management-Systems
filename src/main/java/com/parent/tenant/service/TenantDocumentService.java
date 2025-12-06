package com.parent.tenant.service;

import com.parent.tenant.dto.TenantDocumentDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import java.util.List;

public interface TenantDocumentService {

    List<TenantDocumentDto> getDocuments(Long tenantId);

    TenantDocumentDto addDocument(Long tenantId, TenantDocumentDto dto);

    void deleteDocument(Long tenantId, Long documentId);
    
 // 🔹 NEW: upload real file + save metadata
    TenantDocumentDto uploadDocument(Long tenantId, String type, MultipartFile file);

    // 🔹 NEW: load file for download
    Resource loadDocumentAsResource(Long tenantId, Long documentId);
}
