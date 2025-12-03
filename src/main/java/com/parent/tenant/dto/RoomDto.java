package com.parent.tenant.dto;

public class RoomDto {
    private Long id;
    private String roomNumber;
    private String floor;
    private String sharingType;
    private Integer monthlyRent;
    private Boolean occupied;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public String getSharingType() { return sharingType; }
    public void setSharingType(String sharingType) { this.sharingType = sharingType; }
    public Integer getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(Integer monthlyRent) { this.monthlyRent = monthlyRent; }
    public Boolean getOccupied() { return occupied; }
    public void setOccupied(Boolean occupied) { this.occupied = occupied; }
}
