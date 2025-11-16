package com.parent.pg.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;

@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer capacity;
    private String roomNumber;
    private Double pricePerBed;
    private Boolean available = true;
    private String notes;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomAmenity> amenities;

    @ElementCollection
    @CollectionTable(name = "room_furniture", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "furniture_item")
    private List<String> furniture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pg_id", nullable = false)
    @JsonBackReference(value = "pg-room")
    private PgEntity pg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    @JsonBackReference(value = "floor-room")
    private Floor floor;

    public RoomEntity() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
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

	public List<RoomAmenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<RoomAmenity> amenities) {
		this.amenities = amenities;
	}

	public List<String> getFurniture() {
		return furniture;
	}

	public void setFurniture(List<String> furniture) {
		this.furniture = furniture;
	}

	public PgEntity getPg() {
		return pg;
	}

	public void setPg(PgEntity pg) {
		this.pg = pg;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

    // getters & setters...
    
}
