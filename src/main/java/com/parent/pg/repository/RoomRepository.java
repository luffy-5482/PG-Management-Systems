package com.parent.pg.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.pg.model.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> findByPg_Id(Long pgId);
    List<RoomEntity> findByFloor_Id(Long floorId);
}
