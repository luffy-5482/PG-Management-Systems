package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.RoomRequest;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // ---------------------------------------------------------
    // 🔥 Get all rooms of a PG (only if PG belongs to owner)
    // ---------------------------------------------------------
    @GetMapping("/pgs/{pgId}")
    public List<RoomResponse> getRoomsByPgId(@PathVariable Long pgId) {
        return roomService.getRoomsByPgId(pgId);  // already secured inside service
    }

    // ---------------------------------------------------------
    // 🔥 Get all rooms of a floor (only if floor belongs to owner)
    // ---------------------------------------------------------
    @GetMapping("/floors/{floorId}")
    public List<RoomResponse> getRoomsByFloorId(@PathVariable Long floorId) {
        return roomService.getRoomsByFloorId(floorId);  // already secured
    }

    // ---------------------------------------------------------
    // 🔥 Get room by ID (only if owned)
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);  // secured by service
    }

    // ---------------------------------------------------------
    // 🔥 Create room (PG + floor must belong to owner)
    // ---------------------------------------------------------
    @PostMapping
    public RoomResponse createRoom(@RequestBody RoomRequest request) {
        return roomService.createRoom(request);
    }

    // ---------------------------------------------------------
    // 🔥 Update room (only if owned)
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public RoomResponse updateRoom(@PathVariable Long id, @RequestBody RoomRequest request) {
        return roomService.updateRoom(id, request);
    }

    // ---------------------------------------------------------
    // 🔥 Delete room (only if owned)
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "Room deleted successfully!";
    }
}
