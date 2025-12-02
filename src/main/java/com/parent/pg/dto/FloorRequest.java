package com.parent.pg.dto;

import java.util.List;

public class FloorRequest {

	private Long id; // update / delete
	private Long pgId; // optional
	private Boolean delete; // nested delete flag

	private String floorName;
	private Integer totalRooms;
	private String commonAreas;

	private List<RoomRequest> rooms;

	public FloorRequest() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPgId() {
		return pgId;
	}

	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public Integer getTotalRooms() {
		return totalRooms;
	}

	public void setTotalRooms(Integer totalRooms) {
		this.totalRooms = totalRooms;
	}

	public String getCommonAreas() {
		return commonAreas;
	}

	public void setCommonAreas(String commonAreas) {
		this.commonAreas = commonAreas;
	}

	public List<RoomRequest> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomRequest> rooms) {
		this.rooms = rooms;
	}
}
