package com.parent.pg.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.model.RoomEntity;
import com.parent.pg.service.RoomService;

@RestController
@RequestMapping("/api/pgs/{pgId}")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // get all rooms for a PG
    @GetMapping("/rooms")
    public List<RoomEntity> getRoomsByPg(@PathVariable Long pgId) {
        return roomService.getRoomsByPgId(pgId);
    }

    // get rooms by floor
    @GetMapping("/floors/{floorId}/rooms")
    public List<RoomEntity> getRoomsByFloor(@PathVariable Long pgId, @PathVariable Long floorId) {
        return roomService.getRoomsByFloorId(floorId);
    }

    // create room under pg and floor
    @PostMapping("/floors/{floorId}/rooms")
    public RoomEntity createRoom(@PathVariable Long pgId, @PathVariable Long floorId, @RequestBody RoomEntity room) {
        return roomService.createRoom(pgId, floorId, room);
    }

    @GetMapping("/rooms/{roomId}")
    public RoomEntity getRoomById(@PathVariable Long pgId, @PathVariable Long roomId) {
        return roomService.getRoomById(roomId);
    }

    @PutMapping("/rooms/{roomId}")
    public RoomEntity updateRoom(@PathVariable Long pgId, @PathVariable Long roomId, @RequestBody RoomEntity room) {
        return roomService.updateRoom(roomId, room);
    }

    @DeleteMapping("/rooms/{roomId}")
    public String deleteRoom(@PathVariable Long pgId, @PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return "Room deleted successfully";
    }
}
