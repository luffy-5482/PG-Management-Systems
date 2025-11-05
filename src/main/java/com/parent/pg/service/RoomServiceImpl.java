package com.parent.pg.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.pg.model.PgEntity;
import com.parent.pg.model.Floor;
import com.parent.pg.model.RoomEntity;
import com.parent.pg.repository.PgRepository;
import com.parent.pg.repository.FloorRepository;
import com.parent.pg.repository.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PgRepository pgRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Override
    public List<RoomEntity> getRoomsByPgId(Long pgId) {
        return roomRepository.findByPg_Id(pgId);
    }

    @Override
    public List<RoomEntity> getRoomsByFloorId(Long floorId) {
        return roomRepository.findByFloor_Id(floorId);
    }

    @Override
    public RoomEntity createRoom(Long pgId, Long floorId, RoomEntity room) {
        PgEntity pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + pgId));
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + floorId));

        room.setPg(pg);
        room.setFloor(floor);

        // if amenities/furniture present, they are ElementCollection strings — nothing extra to set
        return roomRepository.save(room);
    }

    @Override
    public RoomEntity getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    @Override
    public RoomEntity updateRoom(Long id, RoomEntity roomDetails) {
        RoomEntity room = getRoomById(id);
        room.setCapacity(roomDetails.getCapacity());
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setPricePerBed(roomDetails.getPricePerBed());
        room.setAvailable(roomDetails.getAvailable());
        room.setNotes(roomDetails.getNotes());
        room.setAmenities(roomDetails.getAmenities());
        room.setFurniture(roomDetails.getFurniture());
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
