package com.parent.pg.service;

import java.util.List;
import com.parent.pg.dto.AmenityRequest;
import com.parent.pg.dto.AmenityResponse;

public interface AmenityService {
    List<AmenityResponse> getAllAmenities();
    AmenityResponse getAmenityById(Long id);
    AmenityResponse createAmenity(AmenityRequest request);
    AmenityResponse updateAmenity(Long id, AmenityRequest request);
    void deleteAmenity(Long id);
}
