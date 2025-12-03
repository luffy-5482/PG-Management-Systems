package com.parent.manager.model;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "manager_tasks")
public class ManagerTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long staffId;
	private Long pgId;

	private String title;
	private String description;

	private String status; // PENDING / COMPLETED

	private Long assignedBy; // managerId

	private Instant assignedAt = Instant.now();
	private Instant completedAt;

	private String proofUrl;

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public Long getStaffId() {
		return staffId;
	}

	public Long getPgId() {
		return pgId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getStatus() {
		return status;
	}

	public Long getAssignedBy() {
		return assignedBy;
	}

	public Instant getAssignedAt() {
		return assignedAt;
	}

	public Instant getCompletedAt() {
		return completedAt;
	}

	public String getProofUrl() {
		return proofUrl;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAssignedBy(Long assignedBy) {
		this.assignedBy = assignedBy;
	}

	public void setAssignedAt(Instant assignedAt) {
		this.assignedAt = assignedAt;
	}

	public void setCompletedAt(Instant completedAt) {
		this.completedAt = completedAt;
	}

	public void setProofUrl(String proofUrl) {
		this.proofUrl = proofUrl;
	}
}
