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

    @GetMapping
    public List<PropertyPhotoResponse> getAllPhotos() {
        return propertyPhotoService.getAllPhotos();
    }

    @GetMapping("/{id}")
    public PropertyPhotoResponse getPhotoById(@PathVariable Long id) {
        return propertyPhotoService.getPhotoById(id);
    }

    @PostMapping
    public PropertyPhotoResponse createPhoto(@RequestBody PropertyPhotoRequest request) {
        return propertyPhotoService.createPhoto(request);
    }

    @PutMapping("/{id}")
    public PropertyPhotoResponse updatePhoto(@PathVariable Long id, @RequestBody PropertyPhotoRequest request) {
        return propertyPhotoService.updatePhoto(id, request);
    }

    @DeleteMapping("/{id}")
    public String deletePhoto(@PathVariable Long id) {
        propertyPhotoService.deletePhoto(id);
        return "Property photo deleted successfully!";
    }
}
