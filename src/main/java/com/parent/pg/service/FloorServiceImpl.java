package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
import com.parent.pg.dto.FloorRequest;
import com.parent.pg.dto.FloorResponse;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.model.Floor;
import com.parent.pg.model.PgEntity;
import com.parent.pg.model.RoomAmenity;
import com.parent.pg.model.RoomEntity;
import com.parent.pg.repository.FloorRepository;
import com.parent.pg.repository.PgRepository;
import com.parent.pg.repository.RoomRepository;

@Service
public class FloorServiceImpl implements FloorService {

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private PgRepository pgRepository;

    @Autowired
    private RoomRepository roomRepository;

    // ---------------------------
    // 🔐 Owner helper
    // ---------------------------
    private Long getOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null)
            throw new RuntimeException("Unauthorized: owner not found in token");
        return id;
    }

    // ---------------------------
    // ♻️ Convert entity → DTO (UPDATED for RoomAmenity)
    // ---------------------------
    private RoomResponse toRoomResponse(RoomEntity room) {

        // 🔥 Extract new amenities list (List<RoomAmenity> → List<String>)
        List<String> amenityNames = (room.getAmenities() == null)
                ? List.of()
                : room.getAmenities()
                      .stream()
                      .map(RoomAmenity::getAmenityName)
                      .collect(Collectors.toList());

        return new RoomResponse(
            room.getId(),
            room.getRoomNumber(),
            room.getCapacity(),
            room.getPricePerBed(),
            room.getAvailable(),
            room.getNotes(),
            amenityNames,                  // ✔ updated
            room.getFurniture(),
            (room.getFloor() != null ? room.getFloor().getId() : null),
            (room.getPg() != null ? room.getPg().getId() : null)
        );
    }

    // ---------------------------
    // ♻️ Floor mapper (no change)
    // ---------------------------
    private FloorResponse toFloorResponse(Floor floor) {

        List<RoomResponse> roomResponses = (floor.getRooms() == null)
                ? List.of()
                : floor.getRooms()
                     .stream()
                     .map(this::toRoomResponse)
                     .collect(Collectors.toList());

        return new FloorResponse(
            floor.getId(),
            floor.getFloorName(),
            floor.getTotalRooms(),
            floor.getCommonAreas(),
            (floor.getPg() != null ? floor.getPg().getId() : null),
            roomResponses
        );
    }

    private void applyFloorRequest(FloorRequest request, Floor floor, PgEntity pg) {
        floor.setPg(pg);
        floor.setFloorName(request.getFloorName());
        floor.setTotalRooms(request.getTotalRooms());
        floor.setCommonAreas(request.getCommonAreas());
    }

    // ---------------------------
    // 🔥 GET FLOORS (secured)
    // ---------------------------
    @Override 
    public List<FloorResponse> getFloorsByPgId(Long pgId) {
        Long ownerId = getOwnerId();

        pgRepository.findByIdAndOwnerId(pgId, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to you"));

        return floorRepository.findByPgIdAndPgOwnerId(pgId, ownerId)
                .stream()
                .map(this::toFloorResponse)
                .collect(Collectors.toList());
    }

    // ---------------------------
    // 🔥 GET SINGLE FLOOR
    // ---------------------------
    @Override
    public FloorResponse getFloorById(Long id) {
        Long ownerId = getOwnerId();

        Floor floor = floorRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized: Floor does not belong to you"));

        return toFloorResponse(floor);
    }

    // ---------------------------
    // 🔥 CREATE FLOOR
    // ---------------------------
    @Override
    public FloorResponse createFloor(FloorRequest request) {
        Long ownerId = getOwnerId();

        PgEntity pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to you"));

        Floor floor = new Floor();
        applyFloorRequest(request, floor, pg);

        Floor saved = floorRepository.save(floor);
        return toFloorResponse(saved);
    }

    // ---------------------------
    // 🔥 UPDATE FLOOR
    // ---------------------------
    @Override
    public FloorResponse updateFloor(Long id, FloorRequest request) {
        Long ownerId = getOwnerId();

        Floor existing = floorRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized: Floor does not belong to you"));

        PgEntity pg = existing.getPg();

        if (request.getPgId() != null) {
            pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to you"));
        }

        applyFloorRequest(request, existing, pg);

        Floor updated = floorRepository.save(existing);
        return toFloorResponse(updated); 
    }

    // ---------------------------
    // 🔥 DELETE FLOOR
    // ---------------------------
    @Override
    public void deleteFloor(Long id) {
        Long ownerId = getOwnerId();

        Floor floor = floorRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized: Floor does not belong to you"));

        floorRepository.delete(floor);
    }
}
