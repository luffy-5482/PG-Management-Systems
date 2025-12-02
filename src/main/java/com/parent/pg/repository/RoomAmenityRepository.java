package com.parent.pg.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.pg.model.RoomAmenity;

public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, Long> {

    List<RoomAmenity> findByRoomId(Long roomId);
    List<RoomAmenity> findByRoomPgOwnerId(Long ownerId);
    RoomAmenity findByIdAndRoomPgOwnerId(Long id, Long ownerId);
}
