package com.parent.pg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.pg.dto.FloorRequest;
import com.parent.pg.dto.FloorResponse;
import com.parent.pg.service.FloorService;

@RestController
@RequestMapping("/api/floors")
@CrossOrigin(origins = "*")
public class FloorController {

    @Autowired
    private FloorService floorService;

    // ---------------------------------------------------------
    // 🔥 Get all floors for a specific PG (only if PG belongs to owner)
    // ---------------------------------------------------------
    @GetMapping("/pgs/{pgId}")
    public List<FloorResponse> getFloorsByPgId(@PathVariable Long pgId) {
        return floorService.getFloorsByPgId(pgId);  // already secured inside service
    }

    // ---------------------------------------------------------
    // 🔥 Get floor by ID (only if owned by logged-in owner)
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public FloorResponse getFloorById(@PathVariable Long id) {
        return floorService.getFloorById(id);  // already secured
    }

    // ---------------------------------------------------------
    // 🔥 Create floor (PG must belong to owner)
    // ---------------------------------------------------------
    @PostMapping
    public FloorResponse createFloor(@RequestBody FloorRequest floorRequest) {
        return floorService.createFloor(floorRequest);
    }

    // ---------------------------------------------------------
    // 🔥 Update floor (only if owned by logged-in owner)
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public FloorResponse updateFloor(@PathVariable Long id, @RequestBody FloorRequest floorRequest) {
        return floorService.updateFloor(id, floorRequest);
    }

    // ---------------------------------------------------------
    // 🔥 Delete floor (only if owned)
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public String deleteFloor(@PathVariable Long id) {
        floorService.deleteFloor(id);
        return "Floor deleted successfully!";
    }
}
