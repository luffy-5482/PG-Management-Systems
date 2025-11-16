package com.parent.pg.service;

import java.util.List;
import com.parent.pg.dto.RoomAmenityRequest;
import com.parent.pg.dto.RoomAmenityResponse;

public interface RoomAmenityService {

    RoomAmenityResponse addAmenity(RoomAmenityRequest request);

    RoomAmenityResponse updateAmenity(Long id, RoomAmenityRequest request);

    void deleteAmenity(Long id);

    List<RoomAmenityResponse> getAmenitiesByRoom(Long roomId);
}
