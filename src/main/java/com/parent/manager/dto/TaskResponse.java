package com.parent.manager.dto;

import java.time.Instant;

public class TaskResponse {
    public Long id;
    public Long staffId;
    public Long pgId;
    public String title;
    public String description;
    public String status;
    public Long assignedBy;
    public Instant assignedAt;
    public Instant completedAt;
    public String proofUrl;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStaffId() {
		return staffId;
	}
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	public Long getPgId() {
		return pgId;
	}
	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getAssignedBy() {
		return assignedBy;
	}
	public void setAssignedBy(Long assignedBy) {
		this.assignedBy = assignedBy;
	}
	public Instant getAssignedAt() {
		return assignedAt;
	}
	public void setAssignedAt(Instant assignedAt) {
		this.assignedAt = assignedAt;
	}
	public Instant getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(Instant completedAt) {
		this.completedAt = completedAt;
	}
	public String getProofUrl() {
		return proofUrl;
	}
	public void setProofUrl(String proofUrl) {
		this.proofUrl = proofUrl;
	}
    
}
