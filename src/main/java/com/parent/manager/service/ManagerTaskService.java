package com.parent.manager.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.parent.manager.dto.TaskRequest;
import com.parent.manager.dto.TaskResponse;
import com.parent.manager.model.ManagerTask;
import com.parent.manager.repository.ManagerTaskRepository;

@Service
public class ManagerTaskService {

    private final ManagerTaskRepository repo;

    public ManagerTaskService(ManagerTaskRepository repo) {
        this.repo = repo;
    }

    public TaskResponse assignTask(Long managerId, TaskRequest req) {
        ManagerTask task = new ManagerTask();
        task.setStaffId(req.staffId);
        task.setPgId(req.pgId);
        task.setTitle(req.title);
        task.setDescription(req.description);
        task.setStatus("PENDING");
        task.setAssignedBy(managerId);
        task.setAssignedAt(Instant.now());

        return toResponse(repo.save(task));
    }

    public TaskResponse markComplete(Long taskId, String proofUrl) {
        ManagerTask task = repo.findById(taskId).orElseThrow();
        task.setStatus("COMPLETED");
        task.setCompletedAt(Instant.now());
        task.setProofUrl(proofUrl);
        return toResponse(repo.save(task));
    }

    public List<TaskResponse> listByPg(Long pgId) {
        return repo.findByPgId(pgId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TaskResponse> listByStaff(Long staffId) {
        return repo.findByStaffId(staffId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private TaskResponse toResponse(ManagerTask t) {
        TaskResponse r = new TaskResponse();
        r.id = t.getId();
        r.staffId = t.getStaffId();
        r.pgId = t.getPgId();
        r.title = t.getTitle();
        r.description = t.getDescription();
        r.status = t.getStatus();
        r.assignedBy = t.getAssignedBy();
        r.assignedAt = t.getAssignedAt();
        r.completedAt = t.getCompletedAt();
        r.proofUrl = t.getProofUrl();
        return r;
    }
}
