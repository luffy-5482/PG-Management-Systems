package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.pg.dto.AmenityRequest;
import com.parent.pg.dto.AmenityResponse;
import com.parent.pg.model.Amenity;
import com.parent.pg.model.PgEntity;
import com.parent.pg.repository.AminityRepo;
import com.parent.pg.repository.PgRepository;

@Service
public class AmenityServiceImpl implements AmenityService {

    @Autowired
    private AminityRepo amenityRepo;

    @Autowired
    private PgRepository pgRepository;

    private AmenityResponse toResponse(Amenity amenity) {
        return new AmenityResponse(
            amenity.getId(),
            amenity.getName(),
            (amenity.getPg() != null ? amenity.getPg().getId() : null)
        );
    }

    @Override
    public List<AmenityResponse> getAllAmenities() {
        return amenityRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public AmenityResponse getAmenityById(Long id) {
        Amenity amenity = amenityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Amenity not found with id: " + id));
        return toResponse(amenity);
    }

    @Override
    public AmenityResponse createAmenity(AmenityRequest request) {
        PgEntity pg = pgRepository.findById(request.getPgId())
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));

        Amenity amenity = new Amenity();
        amenity.setName(request.getName());
        amenity.setPg(pg);

        Amenity saved = amenityRepo.save(amenity);
        return toResponse(saved);
    }

    @Override
    public AmenityResponse updateAmenity(Long id, AmenityRequest request) {
        Amenity existing = amenityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Amenity not found with id: " + id));

        if (request.getPgId() != null) {
            PgEntity pg = pgRepository.findById(request.getPgId())
                    .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));
            existing.setPg(pg);
        }

        existing.setName(request.getName());
        Amenity saved = amenityRepo.save(existing);
        return toResponse(saved);
    }

    @Override
    public void deleteAmenity(Long id) {
        amenityRepo.deleteById(id);
    }
}
