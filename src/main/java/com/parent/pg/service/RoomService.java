package com.parent.pg.service;

import java.util.List;
import com.parent.pg.model.RoomEntity;

public interface RoomService {
    List<RoomEntity> getRoomsByPgId(Long pgId);
    List<RoomEntity> getRoomsByFloorId(Long floorId);
    RoomEntity createRoom(Long pgId, Long floorId, RoomEntity room);
    RoomEntity getRoomById(Long id);
    RoomEntity updateRoom(Long id, RoomEntity roomDetails);
    void deleteRoom(Long id);
}
