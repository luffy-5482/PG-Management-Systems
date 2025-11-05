package com.parent.pg.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.model.Floor;
import com.parent.pg.service.FloorService;

@RestController
@RequestMapping("/api/pgs/{pgId}/floors")
@CrossOrigin(origins = "*")
public class FloorController {

    @Autowired
    private FloorService floorService;

    @GetMapping
    public List<Floor> getFloors(@PathVariable Long pgId) {
        return floorService.getFloorsByPgId(pgId);
    }

    @PostMapping
    public Floor createFloor(@PathVariable Long pgId, @RequestBody Floor floor) {
        return floorService.createFloor(pgId, floor);
    }

    @PutMapping("/{floorId}")
    public Floor updateFloor(@PathVariable Long pgId, @PathVariable Long floorId, @RequestBody Floor floor) {
        return floorService.updateFloor(floorId, floor);
    }

    @DeleteMapping("/{floorId}")
    public String deleteFloor(@PathVariable Long pgId, @PathVariable Long floorId) {
        floorService.deleteFloor(floorId);
        return "Floor deleted successfully";
    }
}
