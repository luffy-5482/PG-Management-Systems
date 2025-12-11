package com.parent.pg.dto;

public class RoomAmenityRequest {
    private Long roomId;
    private String amenityName;

    public RoomAmenityRequest() {}

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getAmenityName() { return amenityName; }
    public void setAmenityName(String amenityName) { this.amenityName = amenityName; }
}
