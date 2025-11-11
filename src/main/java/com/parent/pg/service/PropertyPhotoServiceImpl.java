package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.pg.dto.PropertyPhotoRequest;
import com.parent.pg.dto.PropertyPhotoResponse;
import com.parent.pg.model.PgEntity;
import com.parent.pg.model.PropertyPhoto;
import com.parent.pg.repository.PgRepository;
import com.parent.pg.repository.PropertyPhotoRepository;

@Service
public class PropertyPhotoServiceImpl implements PropertyPhotoService {

    @Autowired
    private PropertyPhotoRepository propertyPhotoRepository;

    @Autowired
    private PgRepository pgRepository;

    private PropertyPhotoResponse toResponse(PropertyPhoto photo) {
        return new PropertyPhotoResponse(
            photo.getId(),
            photo.getImageUrl(),
            photo.getIsMain(),
            (photo.getPg() != null ? photo.getPg().getId() : null)
        );
    }

    @Override
    public List<PropertyPhotoResponse> getAllPhotos() {
        return propertyPhotoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public PropertyPhotoResponse getPhotoById(Long id) {
        PropertyPhoto photo = propertyPhotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property photo not found with id: " + id));
        return toResponse(photo);
    }

    @Override
    public PropertyPhotoResponse createPhoto(PropertyPhotoRequest request) {
        PgEntity pg = pgRepository.findById(request.getPgId())
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));

        PropertyPhoto photo = new PropertyPhoto();
        photo.setImageUrl(request.getImageUrl());
        photo.setIsMain(request.getIsMain());
        photo.setPg(pg);

        PropertyPhoto saved = propertyPhotoRepository.save(photo);
        return toResponse(saved);
    }

    @Override
    public PropertyPhotoResponse updatePhoto(Long id, PropertyPhotoRequest request) {
        PropertyPhoto existing = propertyPhotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property photo not found with id: " + id));

        if (request.getPgId() != null) {
            PgEntity pg = pgRepository.findById(request.getPgId())
                    .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));
            existing.setPg(pg);
        }

        existing.setImageUrl(request.getImageUrl());
        existing.setIsMain(request.getIsMain());
        PropertyPhoto saved = propertyPhotoRepository.save(existing);
        return toResponse(saved);
    }

    @Override
    public void deletePhoto(Long id) {
        propertyPhotoRepository.deleteById(id);
    }
}
