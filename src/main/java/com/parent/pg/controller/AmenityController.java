package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.AmenityRequest;
import com.parent.pg.dto.AmenityResponse;
import com.parent.pg.service.AmenityService;

@RestController
@RequestMapping("/api/amenities")
@CrossOrigin(origins = "*")
public class AmenityController {

    @Autowired
    private AmenityService amenityService;

    // -----------------------------------------------------
    // 🔥 Get ALL amenities of the logged-in owner
    // -----------------------------------------------------
    @GetMapping
    public List<AmenityResponse> getAllAmenities() {
        return amenityService.getAllAmenities(); // already secure
    }

    // -----------------------------------------------------
    // 🔥 Get a specific amenity (only if owned)
    // -----------------------------------------------------
    @GetMapping("/{id}")
    public AmenityResponse getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id); // already secure
    }

    // -----------------------------------------------------
    // 🔥 Create amenity — PG must belong to owner
    // -----------------------------------------------------
    @PostMapping
    public AmenityResponse createAmenity(@RequestBody AmenityRequest request) {
        return amenityService.createAmenity(request);
    }

    // -----------------------------------------------------
    // 🔥 Update amenity — only if owned by logged-in owner
    // -----------------------------------------------------
    @PutMapping("/{id}")
    public AmenityResponse updateAmenity(@PathVariable Long id, @RequestBody AmenityRequest request) {
        return amenityService.updateAmenity(id, request);
    }

    // -----------------------------------------------------
    // 🔥 Delete amenity — only if owned
    // -----------------------------------------------------
    @DeleteMapping("/{id}")
    public String deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return "Amenity deleted successfully!";
    }
}
