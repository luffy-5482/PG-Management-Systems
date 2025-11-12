package com.parent.pg.service;

import java.util.List;
import com.parent.pg.dto.PropertyPhotoRequest;
import com.parent.pg.dto.PropertyPhotoResponse;

public interface PropertyPhotoService {
    List<PropertyPhotoResponse> getAllPhotos();
    PropertyPhotoResponse getPhotoById(Long id);
    PropertyPhotoResponse createPhoto(PropertyPhotoRequest request);
    PropertyPhotoResponse updatePhoto(Long id, PropertyPhotoRequest request);
    void deletePhoto(Long id);
}
