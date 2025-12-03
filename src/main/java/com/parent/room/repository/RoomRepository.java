package com.parent.room.repository;

import com.parent.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // find the room assigned to a particular tenant (if your Room has tenantId or tenant relation)
    Optional<Room> findByTenantId(Long tenantId);

    // list rooms by occupied flag
    List<Room> findByOccupied(boolean occupied);

    // other useful derived queries you might add:
    // List<Room> findByFloor(String floor);
    // Optional<Room> findByRoomNumber(String roomNumber);
}
