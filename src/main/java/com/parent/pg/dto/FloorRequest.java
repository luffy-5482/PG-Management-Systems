package com.parent.pg.dto;

public class FloorRequest {
	private Long pgId;
	private String floorName;
	private Integer totalRooms;
	private String commonAreas;

	public FloorRequest() {
	}

	public Long getPgId() {
		return pgId;
	}

	public void setPgId(Long pgId) {
		this.pgId = pgId;
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
}
