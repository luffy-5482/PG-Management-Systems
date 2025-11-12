package com.parent.pg.service;

import java.util.List;	
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.pg.dto.FloorRequest;
import com.parent.pg.dto.FloorResponse;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.model.Floor;
import com.parent.pg.model.PgEntity;
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
    
    private RoomResponse toRoomResponse(RoomEntity room) {
        return new RoomResponse(
            room.getId(),
            room.getRoomNumber(),
            room.getCapacity(),
            room.getPricePerBed(),
            room.getAvailable(),
            room.getNotes(),
            room.getAmenities(),
            room.getFurniture(),
            (room.getFloor() != null ? room.getFloor().getId() : null),
            (room.getPg() != null ? room.getPg().getId() : null)
        );
    }

    private FloorResponse toFloorResponse(Floor floor) {
        List<RoomResponse> roomResponses = (floor.getRooms() == null) ? List.of()
                : floor.getRooms().stream().map(this::toRoomResponse).collect(Collectors.toList());

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

    @Override
    public List<FloorResponse> getFloorsByPgId(Long pgId) {
        return floorRepository.findByPg_Id(pgId)
                .stream()
                .map(this::toFloorResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FloorResponse getFloorById(Long id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + id));
        return toFloorResponse(floor);
    }

    @Override
    public FloorResponse createFloor(FloorRequest request) {
        PgEntity pg = pgRepository.findById(request.getPgId())
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));

        Floor floor = new Floor();
        applyFloorRequest(request, floor, pg);

        Floor saved = floorRepository.save(floor);
        return toFloorResponse(saved);
    }

    @Override
    public FloorResponse updateFloor(Long id, FloorRequest request) {
        Floor existing = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + id));

        PgEntity pg = existing.getPg();
        if (request.getPgId() != null) {
            pg = pgRepository.findById(request.getPgId())
                    .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));
        }

        applyFloorRequest(request, existing, pg);
        Floor updated = floorRepository.save(existing);
        return toFloorResponse(updated);
    }

    @Override
    public void deleteFloor(Long id) {
        floorRepository.deleteById(id);
    }
}
