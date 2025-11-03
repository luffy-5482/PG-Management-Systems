package com.parent.pg.model;

import jakarta.persistence.*;

@Entity
@Table(name = "amenities")
public class Amenity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name; // e.g., WiFi, Laundry, Parking

	@ManyToOne
	@JoinColumn(name = "pg_id")
	private PgEntity pg;

	public Amenity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PgEntity getPg() {
		return pg;
	}

	public void setPg(PgEntity pg) {
		this.pg = pg;
	}
}
