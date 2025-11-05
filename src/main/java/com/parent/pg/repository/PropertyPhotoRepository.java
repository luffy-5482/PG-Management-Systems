package com.parent.pg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parent.pg.model.PropertyPhoto;

@Repository
public interface PropertyPhotoRepository extends JpaRepository<PropertyPhoto, Long> {
}
