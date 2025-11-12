package com.parent.pg.service;

import java.util.List;

import com.parent.pg.dto.FloorRequest;
import com.parent.pg.dto.FloorResponse;

public interface FloorService {
    List<FloorResponse> getFloorsByPgId(Long pgId);
    FloorResponse getFloorById(Long id);
    FloorResponse createFloor(FloorRequest floorRequest);
    FloorResponse updateFloor(Long id, FloorRequest floorRequest);
    void deleteFloor(Long id);
}
