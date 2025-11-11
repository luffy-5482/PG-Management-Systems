package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.pg.dto.RoomRequest;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.model.Floor;
import com.parent.pg.model.PgEntity;
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

    // ✅ Convert entity → response (now uses setters, no long constructor)
    private RoomResponse toResponse(RoomEntity room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPricePerBed(room.getPricePerBed());
        response.setAvailable(room.getAvailable());
        response.setNotes(room.getNotes());
        response.setAmenities(room.getAmenities());
        response.setFurniture(room.getFurniture());

        if (room.getFloor() != null)
            response.setFloorId(room.getFloor().getId());

        if (room.getPg() != null)
            response.setPgId(room.getPg().getId());

        return response;
    }

    // ✅ Apply request DTO → entity
    private void applyRequest(RoomRequest request, RoomEntity room, PgEntity pg, Floor floor) {
        room.setPg(pg);
        room.setFloor(floor);
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setPricePerBed(request.getPricePerBed());
        room.setAvailable(request.getAvailable());
        room.setNotes(request.getNotes());
        room.setAmenities(request.getAmenities());
        room.setFurniture(request.getFurniture());
    }

    // ✅ Get all rooms by PG
    @Override
    public List<RoomResponse> getRoomsByPgId(Long pgId) {
        return roomRepository.findByPg_Id(pgId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get all rooms by Floor
    @Override
    public List<RoomResponse> getRoomsByFloorId(Long floorId) {
        return roomRepository.findByFloor_Id(floorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get single room
    @Override
    public RoomResponse getRoomById(Long id) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return toResponse(room);
    }

    // ✅ Create room
    @Override
    public RoomResponse createRoom(RoomRequest request) {
        PgEntity pg = pgRepository.findById(request.getPgId())
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));

        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + request.getFloorId()));

        RoomEntity room = new RoomEntity();
        applyRequest(request, room, pg, floor);

        RoomEntity saved = roomRepository.save(room);
        return toResponse(saved);
    }

    // ✅ Update room
    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        RoomEntity existing = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        PgEntity pg = existing.getPg();
        if (request.getPgId() != null) {
            pg = pgRepository.findById(request.getPgId())
                    .orElseThrow(() -> new RuntimeException("PG not found with id: " + request.getPgId()));
        }

        Floor floor = existing.getFloor();
        if (request.getFloorId() != null) {
            floor = floorRepository.findById(request.getFloorId())
                    .orElseThrow(() -> new RuntimeException("Floor not found with id: " + request.getFloorId()));
        }

        applyRequest(request, existing, pg, floor);

        RoomEntity updated = roomRepository.save(existing);
        return toResponse(updated);
    }

    // ✅ Delete room
    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
