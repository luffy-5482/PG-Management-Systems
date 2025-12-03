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
    // 🔐 Owner + Staff helpers
    // ---------------------------
    private Long getOwnerId() {
        return SecurityUtils.getLoggedInOwnerId();
    }


    // ---------------------------
    // ♻️ Convert Room → DTO
    // ---------------------------
    private RoomResponse toRoomResponse(RoomEntity room) {

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
                amenityNames,
                room.getFurniture(),
                (room.getFloor() != null ? room.getFloor().getId() : null),
                (room.getPg() != null ? room.getPg().getId() : null)
        );
    }

    // ---------------------------
    // ♻️ Convert Floor → DTO
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

    // ---------------------------
    // 🧠 OWNER-ONLY — apply full changes
    // ---------------------------
    private void applyOwnerChanges(FloorRequest req, Floor floor, PgEntity pg) {
        floor.setPg(pg);
        floor.setFloorName(req.getFloorName());
        floor.setTotalRooms(req.getTotalRooms());
        floor.setCommonAreas(req.getCommonAreas());
    }

    // ---------------------------
    // 🧠 STAFF — allowed updates (limited)
    // ---------------------------
    private void applyStaffChanges(FloorRequest req, Floor floor) {
        // Staff can update ONLY:
        floor.setCommonAreas(req.getCommonAreas());

        // Staff CANNOT update:
        // floorName, totalRooms, pg
    }

    // ---------------------------
    // GET FLOORS BY PG — Owner + Staff
    // ---------------------------
    @Override
    public List<FloorResponse> getFloorsByPgId(Long pgId) {

        Long ownerId = getOwnerId();

        if (ownerId != null) {
            pgRepository.findByIdAndOwnerId(pgId, ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to owner"));

            return floorRepository.findByPgIdAndPgOwnerId(pgId, ownerId)
                    .stream()
                    .map(this::toFloorResponse)
                    .collect(Collectors.toList());
        }


        throw new RuntimeException("Unauthorized");
    }

    // ---------------------------
    // GET FLOOR BY ID — Owner + Staff
    // ---------------------------
    @Override
    public FloorResponse getFloorById(Long id) {

        Long ownerId = getOwnerId();

        if (ownerId != null) {
            Floor floor = floorRepository.findByIdAndPgOwnerId(id, ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized floor access"));

            return toFloorResponse(floor);
        }


        throw new RuntimeException("Unauthorized");
    }

    // ---------------------------
    // CREATE FLOOR — OWNER ONLY
    // ---------------------------
    @Override
    public FloorResponse createFloor(FloorRequest req) {

        Long ownerId = getOwnerId();

        if (ownerId == null)
            throw new RuntimeException("Unauthorized");

        PgEntity pg = pgRepository.findByIdAndOwnerId(req.getPgId(), ownerId)
                .orElseThrow(() -> new RuntimeException("PG not owned by you"));

        Floor floor = new Floor();
        applyOwnerChanges(req, floor, pg);

        Floor saved = floorRepository.save(floor);
        return toFloorResponse(saved);
    }

    // ---------------------------
    // UPDATE FLOOR — Owner (full), Staff (limited)
    // ---------------------------
    @Override
    public FloorResponse updateFloor(Long id, FloorRequest req) {

        Long ownerId = getOwnerId();

        Floor existing = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found"));


        if (ownerId != null) {
            existing = floorRepository.findByIdAndPgOwnerId(id, ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized floor update"));

            PgEntity pg = existing.getPg();

            if (req.getPgId() != null) {
                pg = pgRepository.findByIdAndOwnerId(req.getPgId(), ownerId)
                        .orElseThrow(() -> new RuntimeException("PG not owned by you"));
            }

            applyOwnerChanges(req, existing, pg);

            Floor updated = floorRepository.save(existing);
            return toFloorResponse(updated);
        }

        throw new RuntimeException("Unauthorized");
    }

    // ---------------------------
    // DELETE FLOOR — OWNER ONLY
    // ---------------------------
    @Override
    public void deleteFloor(Long id) {

        Long ownerId = getOwnerId();


        if (ownerId == null)
            throw new RuntimeException("Unauthorized");

        Floor floor = floorRepository.findByIdAndPgOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized floor deletion"));

        floorRepository.delete(floor);
    }
}
