package com.parent.pg.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.parent.owner.model.Owner;
import com.parent.staff.model.StaffEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pgs")
public class PgEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String type;
	private Double price;
	private String rules;
	private Boolean availability;

	@Embedded
	private Address address;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	@JsonBackReference
	private Owner owner;

	@OneToMany(mappedBy = "pg", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Amenity> amenities;

	@OneToMany(mappedBy = "pg", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<PropertyPhoto> photos;

	@OneToMany(mappedBy = "pg", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "pg-floor")
	private List<Floor> floors;
	
	@OneToMany(mappedBy = "pg", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "pg-room")
	private List<RoomEntity> rooms; // Add this
	
	@OneToMany(mappedBy = "pg", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContactPerson> contacts;
	
	@OneToMany(mappedBy = "pg", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "pg-staff")
	private List<StaffEntity> staff;
	
	
	public List<ContactPerson> getContacts() {
		return contacts;
	}
	
	public void setContacts(List<ContactPerson> contacts) {
		this.contacts = contacts;
	}

	public List<RoomEntity> getRooms() {
		return rooms;
	}
	
	public void setRooms(List<RoomEntity> rooms) {
		this.rooms = rooms;
	}
	
	public List<Floor> getFloors() {
		return floors;
	}

	public void setFloors(List<Floor> floors) {
		this.floors = floors;
	}

	// Constructors
	public PgEntity() {
	}

	// Getters and Setters
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public List<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<Amenity> amenities) {
		this.amenities = amenities;
	}

	public List<PropertyPhoto> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PropertyPhoto> photos) {
		this.photos = photos;
	}

	public List<StaffEntity> getStaff() {
		return staff;
	}

	public void setStaff(List<StaffEntity> staff) {
		this.staff = staff;
	}
	
}
