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

import com.parent.pg.dto.FloorRequest;
import com.parent.pg.dto.FloorResponse;
import com.parent.pg.service.FloorService;

@RestController
@RequestMapping("/api/floors")
@CrossOrigin(origins = "*")
public class FloorController {

    @Autowired
    private FloorService floorService;

    // Get all floors for a PG
    @GetMapping("/pgs/{pgId}")
    public List<FloorResponse> getFloorsByPgId(@PathVariable Long pgId) {
        return floorService.getFloorsByPgId(pgId);
    }

    // Get floor by ID
    @GetMapping("/{id}")
    public FloorResponse getFloorById(@PathVariable Long id) {
        return floorService.getFloorById(id);
    }

    // Create a floor
    @PostMapping
    public FloorResponse createFloor(@RequestBody FloorRequest floorRequest) {
        return floorService.createFloor(floorRequest);
    }

    // Update floor
    @PutMapping("/{id}")
    public FloorResponse updateFloor(@PathVariable Long id, @RequestBody FloorRequest floorRequest) {
        return floorService.updateFloor(id, floorRequest);
    }

    // Delete floor
    @DeleteMapping("/{id}")
    public String deleteFloor(@PathVariable Long id) {
        floorService.deleteFloor(id);
        return "Floor deleted successfully!";
    }
}
