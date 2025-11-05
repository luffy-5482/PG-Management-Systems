package com.parent.pg.service;

import java.util.List;
import com.parent.pg.model.PropertyPhoto;

public interface PropertyPhotoService {
    List<PropertyPhoto> getAllPhotos();
    PropertyPhoto getPhotoById(Long id);
    PropertyPhoto createPhoto(PropertyPhoto propertyPhoto);
    PropertyPhoto updatePhoto(Long id, PropertyPhoto propertyPhoto);
    void deletePhoto(Long id);
}
