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

import com.parent.pg.dto.RoomRequest;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Get all rooms of a PG
    @GetMapping("/pgs/{pgId}")
    public List<RoomResponse> getRoomsByPgId(@PathVariable Long pgId) {
        return roomService.getRoomsByPgId(pgId);
    }

    // Get all rooms on a specific floor
    @GetMapping("/floors/{floorId}")
    public List<RoomResponse> getRoomsByFloorId(@PathVariable Long floorId) {
        return roomService.getRoomsByFloorId(floorId);
    }

    // Get a single room
    @GetMapping("/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    // Create a room
    @PostMapping
    public RoomResponse createRoom(@RequestBody RoomRequest request) {
        return roomService.createRoom(request);
    }

    // Update room
    @PutMapping("/{id}")
    public RoomResponse updateRoom(@PathVariable Long id, @RequestBody RoomRequest request) {
        return roomService.updateRoom(id, request);
    }

    // Delete room
    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "Room deleted successfully!";
    }
}
