package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.parent.config.SecurityUtils;
import com.parent.pg.dto.RoomAmenityRequest;
import com.parent.pg.dto.RoomAmenityResponse;
import com.parent.pg.model.RoomAmenity;
import com.parent.pg.model.RoomEntity;
import com.parent.pg.repository.RoomAmenityRepository;
import com.parent.pg.repository.RoomRepository;

@Service
public class RoomAmenityServiceImpl implements RoomAmenityService {

    @Autowired private RoomAmenityRepository amenityRepo;
    @Autowired private RoomRepository roomRepo;

    private Long getOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null) throw new RuntimeException("Unauthorized");
        return id;
    }

    private RoomAmenityResponse toResponse(RoomAmenity a) {
        return new RoomAmenityResponse(
                a.getId(),
                a.getAmenityName(),
                a.getRoom().getId()
        );
    }

    @Override
    public RoomAmenityResponse addAmenity(RoomAmenityRequest request) {

        Long ownerId = getOwnerId();

        RoomEntity room = roomRepo.findByIdAndPgOwnerId(request.getRoomId(), ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized room"));

        RoomAmenity amenity = new RoomAmenity();
        amenity.setAmenityName(request.getAmenityName());
        amenity.setRoom(room);

        return toResponse(amenityRepo.save(amenity));
    }

    @Override
    public RoomAmenityResponse updateAmenity(Long id, RoomAmenityRequest request) {

        Long ownerId = getOwnerId();

        RoomAmenity existing = amenityRepo.findByIdAndRoomPgOwnerId(id, ownerId);
        if (existing == null)
            throw new RuntimeException("Unauthorized or not found");

        existing.setAmenityName(request.getAmenityName());

        return toResponse(amenityRepo.save(existing));
    }

    @Override
    public void deleteAmenity(Long id) {

        Long ownerId = getOwnerId();

        RoomAmenity existing = amenityRepo.findByIdAndRoomPgOwnerId(id, ownerId);
        if (existing == null)
            throw new RuntimeException("Unauthorized or not found");

        amenityRepo.delete(existing);
    }

    @Override
    public List<RoomAmenityResponse> getAmenitiesByRoom(Long roomId) {

        Long ownerId = getOwnerId();

        // ensure room belongs to owner
        roomRepo.findByIdAndPgOwnerId(roomId, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized room"));

        return amenityRepo.findByRoomId(roomId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
