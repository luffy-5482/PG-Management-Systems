package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.model.PropertyPhoto;
import com.parent.pg.service.PropertyPhotoService;

@RestController
@RequestMapping("/api/property-photos")
@CrossOrigin(origins = "*")
public class PropertyPhotoController {

    @Autowired
    private PropertyPhotoService propertyPhotoService;

    @GetMapping
    public List<PropertyPhoto> getAllPhotos() {
        return propertyPhotoService.getAllPhotos();
    }

    @GetMapping("/{id}")
    public PropertyPhoto getPhotoById(@PathVariable Long id) {
        return propertyPhotoService.getPhotoById(id);
    }

    @PostMapping
    public PropertyPhoto createPhoto(@RequestBody PropertyPhoto propertyPhoto) {
        return propertyPhotoService.createPhoto(propertyPhoto);
    }

    @PutMapping("/{id}")
    public PropertyPhoto updatePhoto(@PathVariable Long id, @RequestBody PropertyPhoto propertyPhoto) {
        return propertyPhotoService.updatePhoto(id, propertyPhoto);
    }

    @DeleteMapping("/{id}")
    public String deletePhoto(@PathVariable Long id) {
        propertyPhotoService.deletePhoto(id);
        return "Property photo deleted successfully!";
    }
}
