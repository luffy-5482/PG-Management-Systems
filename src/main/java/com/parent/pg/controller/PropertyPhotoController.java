package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.PropertyPhotoRequest;
import com.parent.pg.dto.PropertyPhotoResponse;
import com.parent.pg.service.PropertyPhotoService;

@RestController
@RequestMapping("/api/property-photos")
@CrossOrigin(origins = "*")
public class PropertyPhotoController {

    @Autowired
    private PropertyPhotoService propertyPhotoService;

    // ---------------------------------------------------------
    // 🔥 Get ALL photos belonging to the logged-in owner
    // ---------------------------------------------------------
    @GetMapping
    public List<PropertyPhotoResponse> getAllPhotos() {
        return propertyPhotoService.getAllPhotos();  // already secured in service
    }

    // ---------------------------------------------------------
    // 🔥 Get a single photo (only if owned by logged-in owner)
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public PropertyPhotoResponse getPhotoById(@PathVariable Long id) {
        return propertyPhotoService.getPhotoById(id);  // secured
    }

    // ---------------------------------------------------------
    // 🔥 Create a photo (PG must belong to owner)
    // ---------------------------------------------------------
    @PostMapping
    public PropertyPhotoResponse createPhoto(@RequestBody PropertyPhotoRequest request) {
        return propertyPhotoService.createPhoto(request);
    }

    // ---------------------------------------------------------
    // 🔥 Update photo (only if owned by owner)
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public PropertyPhotoResponse updatePhoto(@PathVariable Long id,
                                             @RequestBody PropertyPhotoRequest request) {
        return propertyPhotoService.updatePhoto(id, request);
    }

    // ---------------------------------------------------------
    // 🔥 Delete photo (only if owned by owner)
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public String deletePhoto(@PathVariable Long id) {
        propertyPhotoService.deletePhoto(id);
        return "Property photo deleted successfully!";
    }
}
