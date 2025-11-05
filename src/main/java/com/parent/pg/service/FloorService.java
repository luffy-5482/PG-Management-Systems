package com.parent.pg.service;

import java.util.List;
import com.parent.pg.model.Floor;

public interface FloorService {
    List<Floor> getFloorsByPgId(Long pgId);
    Floor createFloor(Long pgId, Floor floor);
    Floor updateFloor(Long floorId, Floor floor);
    void deleteFloor(Long floorId);
}
