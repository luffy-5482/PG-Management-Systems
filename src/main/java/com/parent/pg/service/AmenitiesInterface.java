package com.parent.pg.service;

import java.util.List;

import com.parent.pg.model.Amenity;

public interface AmenitiesInterface{
	List<Amenity> getAllamenities();
	Amenity getamenityById(Long id);
	Amenity createamenity(Amenity amenity);
	Amenity updateamenity(Long id, Amenity amenity);
	void deleteamenity(Long id);
}
