package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.RoomAmenityRequest;
import com.parent.pg.dto.RoomAmenityResponse;
import com.parent.pg.service.RoomAmenityService;

@RestController
@RequestMapping("/api/room-amenities")
@CrossOrigin(origins = "*")
public class RoomAmenityController {

    @Autowired
    private RoomAmenityService service;

    @PostMapping
    public RoomAmenityResponse addAmenity(@RequestBody RoomAmenityRequest req) {
        return service.addAmenity(req);
    }

    @PutMapping("/{id}")
    public RoomAmenityResponse updateAmenity(@PathVariable Long id, @RequestBody RoomAmenityRequest req) {
        return service.updateAmenity(id, req);
    }

    @DeleteMapping("/{id}")
    public String deleteAmenity(@PathVariable Long id) {
        service.deleteAmenity(id);
        return "Amenity deleted successfully";
    }

    @GetMapping("/room/{roomId}")
    public List<RoomAmenityResponse> getAmenitiesByRoom(@PathVariable Long roomId) {
        return service.getAmenitiesByRoom(roomId);
    }
}
