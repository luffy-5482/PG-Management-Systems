package com.parent.pg.dto;

import java.util.List;

public class RoomRequest {
	private Long pgId;
	private Long floorId;
	private String roomNumber;
	private Integer capacity;
	private Double pricePerBed;
	private Boolean available;
	private String notes;
	private List<String> amenities;
	private List<String> furniture;

	public RoomRequest() {
	}

	// Getters and Setters
	public Long getPgId() {
		return pgId;
	}

	public void setPgId(Long pgId) {
		this.pgId = pgId;
	}

	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Double getPricePerBed() {
		return pricePerBed;
	}

	public void setPricePerBed(Double pricePerBed) {
		this.pricePerBed = pricePerBed;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<String> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<String> amenities) {
		this.amenities = amenities;
	}

	public List<String> getFurniture() {
		return furniture;
	}

	public void setFurniture(List<String> furniture) {
		this.furniture = furniture;
	}
}
