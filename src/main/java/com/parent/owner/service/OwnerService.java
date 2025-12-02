package com.parent.owner.service;

import java.util.List;
import com.parent.owner.dto.OwnerRequest;
import com.parent.owner.dto.OwnerResponse;

public interface OwnerService {
    List<OwnerResponse> getAllOwners();
    OwnerResponse getOwnerById(Long id);
    // OwnerResponse createOwner(OwnerRequest request); // <-- REMOVE THIS
    OwnerResponse updateOwner(Long id, OwnerRequest request);
    void deleteOwner(Long id);
}