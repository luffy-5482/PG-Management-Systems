package com.parent.manager.dto;

public class TaskRequest {
    public Long staffId;
    public Long pgId;
    public String title;
    public String description;
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
    
}



