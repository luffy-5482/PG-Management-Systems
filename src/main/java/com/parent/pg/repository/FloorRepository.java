package com.parent.pg.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.pg.model.Floor;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    List<Floor> findByPg_Id(Long pgId);
}
