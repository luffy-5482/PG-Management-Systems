package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
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

    // -----------------------------------------
    // 🔐 Owner Helper
    // -----------------------------------------
    private Long getOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null)
            throw new RuntimeException("Unauthorized: owner not found in token");
        return id;
    }

    // -----------------------------------------
    // 🔁 Convert Entity → Response (unchanged)
    // -----------------------------------------
    private PropertyPhotoResponse toResponse(PropertyPhoto photo) {
        return new PropertyPhotoResponse(
            photo.getId(),
            photo.getImageUrl(),
            photo.getIsMain(),
            (photo.getPg() != null ? photo.getPg().getId() : null)
        );
    }

    // -----------------------------------------
    // 🔥 Get ALL photos — only for owner’s PGs
    // -----------------------------------------
    @Override
    public List<PropertyPhotoResponse> getAllPhotos() {
        Long ownerId = getOwnerId();

        return propertyPhotoRepository.findByPgOwnerId(ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // -----------------------------------------
    // 🔥 Get photo by ID — must belong to owner
    // -----------------------------------------
    @Override
    public PropertyPhotoResponse getPhotoById(Long id) {
        Long ownerId = getOwnerId();

        PropertyPhoto photo = propertyPhotoRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Photo not found or not owned by you")
                );

        return toResponse(photo);
    }

    // -----------------------------------------
    // 🔥 Create photo — PG must belong to owner
    // -----------------------------------------
    @Override
    public PropertyPhotoResponse createPhoto(PropertyPhotoRequest request) {
        Long ownerId = getOwnerId();

        PgEntity pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: PG does not belong to you")
                );

        PropertyPhoto photo = new PropertyPhoto();
        photo.setImageUrl(request.getImageUrl());
        photo.setIsMain(request.getIsMain());
        photo.setPg(pg);

        PropertyPhoto saved = propertyPhotoRepository.save(photo);
        return toResponse(saved);
    }

    // -----------------------------------------
    // 🔥 Update — only if photo belongs to owner
    // -----------------------------------------
    @Override
    public PropertyPhotoResponse updatePhoto(Long id, PropertyPhotoRequest request) {
        Long ownerId = getOwnerId();

        PropertyPhoto existing = propertyPhotoRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Photo not found or not owned by you")
                );

        if (request.getPgId() != null) {
            PgEntity pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                    .orElseThrow(() ->
                            new RuntimeException("Unauthorized: PG does not belong to you")
                    );
            existing.setPg(pg);
        }

        existing.setImageUrl(request.getImageUrl());
        existing.setIsMain(request.getIsMain());

        PropertyPhoto saved = propertyPhotoRepository.save(existing);
        return toResponse(saved);
    }

    // -----------------------------------------
    // 🔥 Delete — only if owned
    // -----------------------------------------
    @Override
    public void deletePhoto(Long id) {
        Long ownerId = getOwnerId();

        PropertyPhoto photo = propertyPhotoRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Photo not found or not owned by you")
                );

        propertyPhotoRepository.delete(photo);
    }
}
