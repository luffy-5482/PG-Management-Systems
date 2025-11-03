package com.parent.pg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.pg.model.PgEntity;
import com.parent.pg.model.PropertyPhoto;
import com.parent.pg.repository.PgRepository;
import com.parent.pg.repository.PropertyPhotoRepository;

@Service
public class PropertyPhotoServiceImpl implements PropertyPhotoService {

    @Autowired
    private PropertyPhotoRepository propertyPhotoRepository;

    @Autowired
    private PgRepository pgRepository; // to link photos with PG

    @Override
    public List<PropertyPhoto> getAllPhotos() {
        return propertyPhotoRepository.findAll();
    }

    @Override
    public PropertyPhoto getPhotoById(Long id) {
        return propertyPhotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property photo not found with id: " + id));
    }

    @Override
    public PropertyPhoto createPhoto(PropertyPhoto propertyPhoto) {
        if (propertyPhoto.getPg() != null && propertyPhoto.getPg().getId() != null) {
            PgEntity pg = pgRepository.findById(propertyPhoto.getPg().getId())
                    .orElseThrow(() -> new RuntimeException("PG not found with id: " + propertyPhoto.getPg().getId()));
            propertyPhoto.setPg(pg);
        }
        return propertyPhotoRepository.save(propertyPhoto);
    }

    @Override
    public PropertyPhoto updatePhoto(Long id, PropertyPhoto updatedPhoto) {
        PropertyPhoto existingPhoto = getPhotoById(id);
        existingPhoto.setImageUrl(updatedPhoto.getImageUrl());
        existingPhoto.setIsMain(updatedPhoto.getIsMain());

        if (updatedPhoto.getPg() != null && updatedPhoto.getPg().getId() != null) {
            PgEntity pg = pgRepository.findById(updatedPhoto.getPg().getId())
                    .orElseThrow(() -> new RuntimeException("PG not found with id: " + updatedPhoto.getPg().getId()));
            existingPhoto.setPg(pg);
        }
        return propertyPhotoRepository.save(existingPhoto);
    }

    @Override
    public void deletePhoto(Long id) {
        propertyPhotoRepository.deleteById(id);
    }
}
