package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
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

    // -----------------------------------
    // 🔐 Get logged-in owner ID
    // -----------------------------------
    private Long getOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null)
            throw new RuntimeException("Unauthorized: Owner not found in token");
        return id;
    }

    // -----------------------------------
    // 🔁 Convert entity → response
    // -----------------------------------
    private AmenityResponse toResponse(Amenity amenity) {
        return new AmenityResponse(
            amenity.getId(),
            amenity.getName(),
            (amenity.getPg() != null ? amenity.getPg().getId() : null)
        );
    }

    // -----------------------------------
    // 🔥 Get ALL amenities → only owner's PG amenities
    // -----------------------------------
    @Override
    public List<AmenityResponse> getAllAmenities() {
        Long ownerId = getOwnerId();

        return amenityRepo.findByPgOwnerId(ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // -----------------------------------
    // 🔥 Get amenity by ID → only if owned
    // -----------------------------------
    @Override
    public AmenityResponse getAmenityById(Long id) {
        Long ownerId = getOwnerId();

        Amenity amenity = amenityRepo.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Amenity not found OR does not belong to you"));

        return toResponse(amenity);
    }

    // -----------------------------------
    // 🔥 Create amenity → PG must belong to owner
    // -----------------------------------
    @Override
    public AmenityResponse createAmenity(AmenityRequest request) {
        Long ownerId = getOwnerId();

        PgEntity pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: PG does not belong to you"));

        Amenity amenity = new Amenity();
        amenity.setName(request.getName());
        amenity.setPg(pg);

        Amenity saved = amenityRepo.save(amenity);
        return toResponse(saved);
    }

    // -----------------------------------
    // 🔥 Update → only if owned + PG owned
    // -----------------------------------
    @Override
    public AmenityResponse updateAmenity(Long id, AmenityRequest request) {
        Long ownerId = getOwnerId();

        Amenity existing = amenityRepo.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Amenity not found or not owned by you")
                );

        if (request.getPgId() != null) {
            PgEntity pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                    .orElseThrow(() ->
                            new RuntimeException("Unauthorized: PG does not belong to you"));
            existing.setPg(pg);
        }

        existing.setName(request.getName());

        Amenity saved = amenityRepo.save(existing);
        return toResponse(saved);
    }

    // -----------------------------------
    // 🔥 Delete → only if owned
    // -----------------------------------
    @Override
    public void deleteAmenity(Long id) {
        Long ownerId = getOwnerId();

        Amenity amenity = amenityRepo.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Amenity not found or not owned by you"));

        amenityRepo.delete(amenity);
    }
}
