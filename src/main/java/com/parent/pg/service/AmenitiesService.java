package com.parent.pg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.parent.pg.model.Amenity;
import com.parent.pg.repository.AminityRepo;

@Service
public class AmenitiesService implements AmenitiesInterface {
	
	AminityRepo Repo;
	 public AmenitiesService(AminityRepo repo) { 
	        this.Repo = repo;
	    }
	@Override
	public List<Amenity> getAllamenities() {
		return Repo.findAll();	
	}

	@Override
	public Amenity getamenityById(Long id) {
		return Repo.findById(id).orElseThrow(() -> new RuntimeException("Amenity not found with id: " + id));
	}

	@Override
	public Amenity createamenity(Amenity amenity) {
		return Repo.save(amenity);
	}

	@Override
	public Amenity updateamenity(Long id, Amenity amenity) {
		Amenity existingAmenity = getamenityById(id);
		existingAmenity.setName(amenity.getName());
		Repo.save(existingAmenity);
		return null;
	}

	@Override
	public void deleteamenity(Long id) {
		Repo.deleteById(id);
	}
	
}
