package com.parent.pg.dto;

public class RoomAmenityResponse {
    private Long id;
    private String amenityName;
    private Long roomId;

    public RoomAmenityResponse(Long id, String amenityName, Long roomId) {
        this.id = id;
        this.amenityName = amenityName;
        this.roomId = roomId;
    }

    public Long getId() { return id; }
    public String getAmenityName() { return amenityName; }
    public Long getRoomId() { return roomId; }
}
