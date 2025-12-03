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
    // 🔐 Get logged-in OWNER ID
    // ---------------------------------------------------------
    private Long getOwnerId() {
        return SecurityUtils.getLoggedInOwnerId();
    }

    // ---------------------------------------------------------
    // 🔐 Get logged-in STAFF info
    // ---------------------------------------------------------
    
    // ---------------------------------------------------------
    // 🚀 Convert entity → response
    // ---------------------------------------------------------
    private RoomResponse toResponse(RoomEntity room) {

        List<String> amenityNames = (room.getAmenities() == null)
                ? List.of()
                : room.getAmenities().stream()
                      .map(RoomAmenity::getAmenityName)
                      .collect(Collectors.toList());

        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerBed(room.getPricePerBed());
        response.setAvailable(room.getAvailable());
        response.setNotes(room.getNotes());
        response.setAmenities(amenityNames);
        response.setFurniture(room.getFurniture());

        if (room.getFloor() != null)
            response.setFloorId(room.getFloor().getId());

        if (room.getPg() != null)
            response.setPgId(room.getPg().getId());

        return response;
    }

    // ---------------------------------------------------------
    // 🧠 Owner-only — Apply all fields
    // ---------------------------------------------------------
    private void applyOwnerChanges(RoomRequest req, RoomEntity room, PgEntity pg, Floor floor) {

        room.setPg(pg);
        room.setFloor(floor);

        room.setRoomNumber(req.getRoomNumber());
        room.setCapacity(req.getCapacity());
        room.setPricePerBed(req.getPricePerBed());
        room.setAvailable(req.getAvailable());
        room.setNotes(req.getNotes());

        // Amenities
        if (req.getAmenities() != null) {
            List<RoomAmenity> list = req.getAmenities().stream()
                    .map(a -> {
                        RoomAmenity am = new RoomAmenity();
                        am.setAmenityName(a);
                        am.setRoom(room);
                        return am;
                    })
                    .collect(Collectors.toList());

            room.setAmenities(list);
        }

        room.setFurniture(req.getFurniture());
    }

    // ---------------------------------------------------------
    // 🧠 STAFF update rules (Option 1)
    // ---------------------------------------------------------
    private void applyStaffChanges(RoomRequest req, RoomEntity room) {

        // Allowed
        room.setAvailable(req.getAvailable());
        room.setNotes(req.getNotes());
        room.setFurniture(req.getFurniture());

        if (req.getAmenities() != null) {
            List<RoomAmenity> list = req.getAmenities().stream()
                    .map(a -> {
                        RoomAmenity am = new RoomAmenity();
                        am.setAmenityName(a);
                        am.setRoom(room);
                        return am;
                    })
                    .collect(Collectors.toList());

            room.setAmenities(list);
        }

        // Not allowed → ignore:
        // roomNumber, capacity, pricePerBed, floor, pg
    }

    // ---------------------------------------------------------
    // GET ROOMS BY PG (Owner + Staff)
    // ---------------------------------------------------------
    @Override
    public List<RoomResponse> getRoomsByPgId(Long pgId) {

        Long ownerId = getOwnerId();

        if (ownerId != null) {
            // OWNER
            pgRepository.findByIdAndOwnerId(pgId, ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to you"));

            return roomRepository.findByPgIdAndPgOwnerId(pgId, ownerId)
                    .stream().map(this::toResponse)
                    .collect(Collectors.toList());
        }


        throw new RuntimeException("Unauthorized");
    }

    // ---------------------------------------------------------
    // GET ROOMS BY FLOOR
    // ---------------------------------------------------------
    @Override
    public List<RoomResponse> getRoomsByFloorId(Long floorId) {

        Long ownerId = getOwnerId();

        if (ownerId != null) {
            floorRepository.findByIdAndPgOwnerId(floorId, ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized: Floor not owned"));

            return roomRepository.findByFloorIdAndPgOwnerId(floorId, ownerId)
                    .stream().map(this::toResponse)
                    .collect(Collectors.toList());
        }


        throw new RuntimeException("Unauthorized");
    }

    // ---------------------------------------------------------
    // GET ROOM BY ID
    // ---------------------------------------------------------
    @Override
    public RoomResponse getRoomById(Long id) {

        Long ownerId = getOwnerId();

        if (ownerId != null) {
            RoomEntity room = roomRepository.findByIdAndPgOwnerId(id, ownerId)
                    .orElseThrow(() -> new RuntimeException("Room not found or unauthorized"));

            return toResponse(room);
        }


        throw new RuntimeException("Unauthorized");
    }

    // ---------------------------------------------------------
    // CREATE ROOM (OWNER ONLY)
    // ---------------------------------------------------------
    @Override
    public RoomResponse createRoom(RoomRequest req) {

        Long ownerId = getOwnerId();

        if (ownerId == null)
            throw new RuntimeException("Unauthorized");

        PgEntity pg = pgRepository.findByIdAndOwnerId(req.getPgId(), ownerId)
                .orElseThrow(() -> new RuntimeException("PG not owned by you"));

        Floor floor = floorRepository.findByIdAndPgOwnerId(req.getFloorId(), ownerId)
                .orElseThrow(() -> new RuntimeException("Floor not owned by you"));

        RoomEntity room = new RoomEntity();
        applyOwnerChanges(req, room, pg, floor);

        RoomEntity saved = roomRepository.save(room);
        return toResponse(saved);
    }

    // ---------------------------------------------------------
    // UPDATE ROOM (OWNER = full update, STAFF = limited update)
    // ---------------------------------------------------------
    @Override
    public RoomResponse updateRoom(Long id, RoomRequest req) {

        Long ownerId = getOwnerId();

        RoomEntity existing = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // STAFF update

        // OWNER update
        if (ownerId != null) {

            existing = roomRepository.findByIdAndPgOwnerId(id, ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized"));

            PgEntity pg = existing.getPg();
            if (req.getPgId() != null) {
                pg = pgRepository.findByIdAndOwnerId(req.getPgId(), ownerId)
                        .orElseThrow(() -> new RuntimeException("PG not owned by you"));
            }

            Floor floor = existing.getFloor();
            if (req.getFloorId() != null) {
                floor = floorRepository.findByIdAndPgOwnerId(req.getFloorId(), ownerId)
                        .orElseThrow(() -> new RuntimeException("Floor not owned by you"));
            }

            applyOwnerChanges(req, existing, pg, floor);

            RoomEntity updated = roomRepository.save(existing);
            return toResponse(updated);
        }

        throw new RuntimeException("Unauthorized");
    }

    // ---------------------------------------------------------
    // DELETE ROOM (OWNER ONLY)
    // ---------------------------------------------------------
    @Override
    public void deleteRoom(Long id) {

        Long ownerId = getOwnerId();


        if (ownerId == null)
            throw new RuntimeException("Unauthorized");

        RoomEntity room = roomRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        roomRepository.delete(room);
    }
}
