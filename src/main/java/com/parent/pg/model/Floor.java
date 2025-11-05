package com.parent.pg.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "floors")
public class Floor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String floorName;
	private Integer totalRooms;

	@Column(length = 1000)
	private String commonAreas;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pg_id", nullable = false)
	@JsonBackReference(value = "pg-floor")
	private PgEntity pg;

	@OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "floor-room")
	private List<RoomEntity> rooms;

	public Floor() {
	}

	// Getters & Setters
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

	public PgEntity getPg() {
		return pg;
	}

	public void setPg(PgEntity pg) {
		this.pg = pg;
	}

	public List<RoomEntity> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomEntity> rooms) {
		this.rooms = rooms;
	}
}
