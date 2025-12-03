package com.parent.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parent.manager.dto.TaskRequest;
import com.parent.manager.dto.TaskResponse;
import com.parent.manager.service.ManagerTaskService;

@RestController
@RequestMapping("/api/manager/tasks")
@CrossOrigin("*")
public class ManagerTaskController {

    private final ManagerTaskService service;

    public ManagerTaskController(ManagerTaskService service) {
        this.service = service;
    }

    @PostMapping("/assign")
    public ResponseEntity<TaskResponse> assignTask(
            @RequestAttribute("managerId") Long managerId,
            @RequestBody TaskRequest req) {
        return ResponseEntity.ok(service.assignTask(managerId, req));
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponse> completeTask(
            @PathVariable Long taskId,
            @RequestParam String proofUrl) {
        return ResponseEntity.ok(service.markComplete(taskId, proofUrl));
    }

    @GetMapping("/pg/{pgId}")
    public ResponseEntity<List<TaskResponse>> listByPg(@PathVariable Long pgId) {
        return ResponseEntity.ok(service.listByPg(pgId));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<TaskResponse>> listByStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok(service.listByStaff(staffId));
    }
}
