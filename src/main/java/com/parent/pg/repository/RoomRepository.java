package com.parent.pg.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.pg.model.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    // existing
    List<RoomEntity> findByPg_Id(Long pgId);
    List<RoomEntity> findByFloor_Id(Long floorId);

    // required for owner security
    Optional<RoomEntity> findByIdAndPgOwnerId(Long id, Long ownerId);
    List<RoomEntity> findByPgIdAndPgOwnerId(Long pgId, Long ownerId);
    List<RoomEntity> findByFloorIdAndPgOwnerId(Long floorId, Long ownerId);
}
