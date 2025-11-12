package com.parent.pg.service;

import java.util.List;
import com.parent.pg.dto.RoomRequest;
import com.parent.pg.dto.RoomResponse;

public interface RoomService {
    List<RoomResponse> getRoomsByPgId(Long pgId);
    List<RoomResponse> getRoomsByFloorId(Long floorId);
    RoomResponse getRoomById(Long id);
    RoomResponse createRoom(RoomRequest request);
    RoomResponse updateRoom(Long id, RoomRequest request);
    void deleteRoom(Long id);
}
