package com.parent.pg.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.pg.model.Floor;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    // your existing
    List<Floor> findByPg_Id(Long pgId);

    // required for owner-based access
    List<Floor> findByPgIdAndPgOwnerId(Long pgId, Long ownerId);
    Optional<Floor> findByIdAndPgOwnerId(Long id, Long ownerId);
}
