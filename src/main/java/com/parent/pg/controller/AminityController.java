package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.pg.model.Amenity;
import com.parent.pg.service.AmenitiesService;

@RestController
@RequestMapping("/api/amenities")
@CrossOrigin(origins = "*")
public class AminityController {
	
	@Autowired
	private AmenitiesService AmenitiesService;
	@GetMapping
	public List<Amenity> getAllamenities() {
		return AmenitiesService.getAllamenities();
	}
	@GetMapping("/{id}")
	public Amenity getamenityById(@PathVariable Long id) {
		return AmenitiesService.getamenityById(id);
	}
	@PostMapping
	public Amenity createamenity(@RequestBody Amenity amenity) {
		return AmenitiesService.createamenity(amenity);
	}
	@PutMapping("/{id}")
	public Amenity updateamenity(@PathVariable Long id, @RequestBody Amenity amenity) {
		return AmenitiesService.updateamenity(id, amenity);
	}
	@DeleteMapping("/{id}")
	public String deleteamenity(@PathVariable Long id) {
		AmenitiesService.deleteamenity(id);
		return "Amenity deleted successfully!";
	}
}
