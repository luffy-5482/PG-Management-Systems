package com.parent.tenant.enums;

public enum TenantStatus {
    DRAFT,                     // applicationId exists, nothing else
    PG_VERIFIED,               // optional (can be removed later)
    PERSONAL_DETAILS_SUBMITTED,
    DOCUMENTS_SUBMITTED,
    UNDER_REVIEW,
    APPROVED,
    ACTIVE,
    SUSPENDED,
    REJECTED,
    EXITED
}
