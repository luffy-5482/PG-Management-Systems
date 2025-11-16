package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
import com.parent.pg.dto.RoomRequest;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.model.Floor;
import com.parent.pg.model.PgEntity;
import com.parent.pg.model.RoomAmenity;
import com.parent.pg.model.RoomEntity;
import com.parent.pg.repository.FloorRepository;
import com.parent.pg.repository.PgRepository;
import com.parent.pg.repository.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PgRepository pgRepository;

    @Autowired
    private FloorRepository floorRepository;

    // ---------------------------------------------------------
    // 🚀 Helper: Get logged-in owner ID (safe)
    // ---------------------------------------------------------
    private Long getOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null)
            throw new RuntimeException("Unauthorized: Owner not found in token");
        return id;
    }

    // ---------------------------------------------------------
    // 🚀 Convert entity → response (UPDATED FOR ROOM AMENITIES)
    // ---------------------------------------------------------
    private RoomResponse toResponse(RoomEntity room) {

        // Convert List<RoomAmenity> → List<String>
        List<String> amenityNames = (room.getAmenities() == null)
                ? List.of()
                : room.getAmenities()
                      .stream()
                      .map(RoomAmenity::getAmenityName)
                      .collect(Collectors.toList());

        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerBed(room.getPricePerBed());
        response.setAvailable(room.getAvailable());
        response.setNotes(room.getNotes());
        response.setAmenities(amenityNames);   // 🔥 FIXED
        response.setFurniture(room.getFurniture());

        if (room.getFloor() != null)
            response.setFloorId(room.getFloor().getId());

        if (room.getPg() != null)
            response.setPgId(room.getPg().getId());

        return response;
    }

    // ---------------------------------------------------------
    // 🚀 Apply request DTO → entity (UPDATED FOR ROOM AMENITIES)
    // ---------------------------------------------------------
    private void applyRequest(RoomRequest request, RoomEntity room, PgEntity pg, Floor floor) {

        room.setPg(pg);
        room.setFloor(floor);
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setPricePerBed(request.getPricePerBed());
        room.setAvailable(request.getAvailable());
        room.setNotes(request.getNotes());

        // -----------------------------
        // 🔥 Convert List<String> → List<RoomAmenity>
        // -----------------------------
        if (request.getAmenities() != null) {
            List<RoomAmenity> amenityEntities = request.getAmenities()
                    .stream()
                    .map(a -> {
                        RoomAmenity ra = new RoomAmenity();
                        ra.setAmenityName(a);
                        ra.setRoom(room);
                        return ra;
                    })
                    .collect(Collectors.toList());

            room.setAmenities(amenityEntities);
        }

        room.setFurniture(request.getFurniture());
    }

    // ---------------------------------------------------------
    // 🔥 Get all rooms by PG — ONLY IF PG belongs to owner
    // ---------------------------------------------------------
    @Override
    public List<RoomResponse> getRoomsByPgId(Long pgId) {
        Long ownerId = getOwnerId();

        pgRepository.findByIdAndOwnerId(pgId, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: PG does not belong to you"));

        return roomRepository.findByPgIdAndPgOwnerId(pgId, ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // 🔥 Get rooms by Floor — only if the floor belongs to owner
    // ---------------------------------------------------------
    @Override
    public List<RoomResponse> getRoomsByFloorId(Long floorId) {
        Long ownerId = getOwnerId();

        floorRepository.findByIdAndPgOwnerId(floorId, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Floor does not belong to you"));

        return roomRepository.findByFloorIdAndPgOwnerId(floorId, ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // 🔥 Get single room — only if room belongs to owner
    // ---------------------------------------------------------
    @Override
    public RoomResponse getRoomById(Long id) {
        Long ownerId = getOwnerId();

        RoomEntity room = roomRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Room not found OR not owned by you"));

        return toResponse(room);
    }

    // ---------------------------------------------------------
    // 🔥 Create room — PG + Floor must belong to owner
    // ---------------------------------------------------------
    @Override
    public RoomResponse createRoom(RoomRequest request) {
        Long ownerId = getOwnerId();

        PgEntity pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: PG does not belong to you"));

        Floor floor = floorRepository.findByIdAndPgOwnerId(request.getFloorId(), ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Floor does not belong to you"));

        RoomEntity room = new RoomEntity();
        applyRequest(request, room, pg, floor);

        RoomEntity saved = roomRepository.save(room);
        return toResponse(saved);
    }

    // ---------------------------------------------------------
    // 🔥 Update room — only if room belongs to owner
    // ---------------------------------------------------------
    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Long ownerId = getOwnerId();

        RoomEntity existing = roomRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Room not found or not owned by you"));

        PgEntity pg = existing.getPg();
        if (request.getPgId() != null) {
            pg = pgRepository.findByIdAndOwnerId(request.getPgId(), ownerId)
                    .orElseThrow(() ->
                            new RuntimeException("Unauthorized: PG does not belong to you"));
        }

        Floor floor = existing.getFloor();
        if (request.getFloorId() != null) {
            floor = floorRepository.findByIdAndPgOwnerId(request.getFloorId(), ownerId)
                    .orElseThrow(() ->
                            new RuntimeException("Unauthorized: Floor does not belong to you"));
        }

        applyRequest(request, existing, pg, floor);

        RoomEntity updated = roomRepository.save(existing);
        return toResponse(updated);
    }

    // ---------------------------------------------------------
    // 🔥 Delete room — only if owned
    // ---------------------------------------------------------
    @Override
    public void deleteRoom(Long id) {
        Long ownerId = getOwnerId();

        RoomEntity room = roomRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Unauthorized: Room not found or not owned by you"));

        roomRepository.delete(room);
    }
}
