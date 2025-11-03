package com.parent.pg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.pg.model.Amenity;

public interface AminityRepo extends JpaRepository<Amenity, Long> {

}
