package com.parent.pg.dto;

import java.util.List;

public class FloorResponse {
	private Long id;
	private String floorName;
	private Integer totalRooms;
	private String commonAreas;
	private Long pgId;
	private List<RoomResponse> rooms;

	public FloorResponse() {
	}

	public FloorResponse(Long id, String floorName, int totalRooms, String commonAreas, Long pgId,
			List<RoomResponse> rooms) {
		this.id = id;
		this.floorName = floorName;
		this.totalRooms = totalRooms;
		this.commonAreas = commonAreas;
		this.pgId = pgId;
		this.rooms = rooms;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public int getTotalRooms() {
		return totalRooms;
	}

	public void setTotalRooms(int totalRooms) {
		this.totalRooms = totalRooms;
	}

	public String getCommonAreas() {
		return commonAreas;
	}

	public void setCommonAreas(String commonAreas) {
		this.commonAreas = commonAreas;
	}

	public Long getPgId() {
		return pgId;
	}

	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}

	public List<RoomResponse> getRooms() { 
		return rooms;
	}

	public void setRooms(List<RoomResponse> rooms) {
		this.rooms = rooms;
	}
}
