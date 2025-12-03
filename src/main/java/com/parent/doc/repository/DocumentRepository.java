package com.parent.doc.repository;

import com.parent.doc.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTenantId(Long tenantId);

    // or if Document has tenant relation:
    // List<Document> findByTenant_Id(Long tenantId);
}
