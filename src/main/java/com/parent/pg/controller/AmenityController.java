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

    @GetMapping
    public List<AmenityResponse> getAllAmenities() {
        return amenityService.getAllAmenities();
    }

    @GetMapping("/{id}")
    public AmenityResponse getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id);
    }

    @PostMapping
    public AmenityResponse createAmenity(@RequestBody AmenityRequest request) {
        return amenityService.createAmenity(request);
    }

    @PutMapping("/{id}")
    public AmenityResponse updateAmenity(@PathVariable Long id, @RequestBody AmenityRequest request) {
        return amenityService.updateAmenity(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return "Amenity deleted successfully!";
    }
}
