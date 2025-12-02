package com.parent.pg.model;

import jakarta.persistence.*;

@Entity
@Table(name = "room_amenity")
public class RoomAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String amenityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    public RoomAmenity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAmenityName() { return amenityName; }
    public void setAmenityName(String amenityName) { this.amenityName = amenityName; }

    public RoomEntity getRoom() { return room; }
    public void setRoom(RoomEntity room) { this.room = room; }
}
