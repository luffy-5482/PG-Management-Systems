package com.parent.pg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.pg.model.PgEntity;

public interface PgRepository extends JpaRepository<PgEntity, Long> {
	List<PgEntity> findByAddress_City(String city);

	List<PgEntity> findByTypeAndAvailability(String type, Boolean availability);
}
