package com.parent.pg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parent.config.SecurityUtils;
import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;
import com.parent.pg.dto.*;
import com.parent.pg.model.*;
import com.parent.pg.repository.*;
import com.parent.staff.dto.StaffResponse;

@Service
public class PgServiceImpl implements PgService {

	@Autowired
	private PgRepository pgRepository;
	@Autowired
	private OwnerRepository ownerRepository;

	// Repositories used for nested operations
	@Autowired
	private ContactPersonRepository contactRepo;
	@Autowired
	private AminityRepo amenityRepo;
	@Autowired
	private PropertyPhotoRepository propertyPhotoRepository;
	@Autowired
	private FloorRepository floorRepository;
	@Autowired
	private RoomRepository roomRepository;

	// ------------------ MAPPERS ------------------

	private AmenityResponse toAmenityResponse(Amenity amenity) {
		return new AmenityResponse(amenity.getId(), amenity.getName(),
				(amenity.getPg() != null ? amenity.getPg().getId() : null));
	}

	private PropertyPhotoResponse toPhotoResponse(PropertyPhoto photo) {
		return new PropertyPhotoResponse(photo.getId(), photo.getImageUrl(), photo.getIsMain(),
				(photo.getPg() != null ? photo.getPg().getId() : null));
	}

	private RoomResponse toRoomResponse(RoomEntity room) {

		List<String> amenityNames = (room.getAmenities() == null) ? List.of()
				: room.getAmenities().stream().map(RoomAmenity::getAmenityName).collect(Collectors.toList());

		return new RoomResponse(room.getId(), room.getRoomNumber(), room.getCapacity(), room.getPricePerBed(),
				room.getAvailable(), room.getNotes(), amenityNames, room.getFurniture(),
				(room.getFloor() != null ? room.getFloor().getId() : null),
				(room.getPg() != null ? room.getPg().getId() : null));
	}

	private FloorResponse toFloorResponse(Floor floor) {
		if (floor == null)
			return null;

		int totalRooms = (floor.getTotalRooms() != null) ? floor.getTotalRooms() : 0;

		List<RoomResponse> roomResponses = (floor.getRooms() == null) ? List.of()
				: floor.getRooms().stream().map(this::toRoomResponse).collect(Collectors.toList());

		return new FloorResponse(floor.getId(), floor.getFloorName(), totalRooms, floor.getCommonAreas(),
				(floor.getPg() != null ? floor.getPg().getId() : null), roomResponses);
	}

	private ContactPersonResponse toContactResponse(ContactPerson c) {
		return new ContactPersonResponse(c.getId(), c.getName(), c.getEmail(), c.getPhoneNumber(), c.getRole(),
				c.getIsPrimary(), (c.getPg() != null ? c.getPg().getId() : null));
	}

	// ------------------ PG → Response (OWNER FULL / STAFF LIMITED)
	// ------------------

	private PgResponse toPgResponse(PgEntity pg) {

		Long ownerId = SecurityUtils.getLoggedInOwnerId();
		Long staffId = SecurityUtils.getLoggedInStaffId();

		boolean isOwner = ownerId != null;
		boolean isStaff = staffId != null;

		List<FloorResponse> floorResponses = (pg.getFloors() == null) ? List.of()
				: pg.getFloors().stream().map(this::toFloorResponse).collect(Collectors.toList());

		List<AmenityResponse> amenityResponses = (pg.getAmenities() == null) ? List.of()
				: pg.getAmenities().stream().map(this::toAmenityResponse).collect(Collectors.toList());

		List<PropertyPhotoResponse> photoResponses = (pg.getPhotos() == null) ? List.of()
				: pg.getPhotos().stream().map(this::toPhotoResponse).collect(Collectors.toList());

		List<ContactPersonResponse> contactResponses = (pg.getContacts() == null) ? List.of()
				: pg.getContacts().stream().map(this::toContactResponse).collect(Collectors.toList());

		if (isOwner) {

			List<StaffResponse> staffResponses = (pg.getStaff() == null) ? List.of()
					: pg.getStaff().stream().map(staff -> {
						StaffResponse s = new StaffResponse();
						s.setId(staff.getId());
						s.setFullName(staff.getFullName());
						s.setEmail(staff.getEmail());
						s.setPhone(staff.getPhone());
						s.setDesignation(staff.getDesignation());
						s.setJoinDate(staff.getJoinDate() != null ? staff.getJoinDate().toString() : null);
						s.setShiftTiming(staff.getShiftTiming());
						s.setActive(staff.getActive());
						s.setPgId(pg.getId());
						return s;
					}).collect(Collectors.toList());

			return new PgResponse(pg.getId(), pg.getName(), pg.getType(), pg.getPrice(), pg.getRules(),
					pg.getAvailability(), pg.getAddress(), (pg.getOwner() != null ? pg.getOwner().getId() : null),
					(pg.getOwner() != null ? pg.getOwner().getFullName() : null),
					(pg.getOwner() != null ? pg.getOwner().getEmail() : null), floorResponses, amenityResponses,
					photoResponses, contactResponses, staffResponses);
		}

		if (isStaff) {

			return new PgResponse(pg.getId(), pg.getName(), pg.getType(), null, // staff cannot see price
					pg.getRules(), pg.getAvailability(), pg.getAddress(), null, // hide ownerId
					null, // hide ownerName
					null, // hide ownerEmail
					floorResponses, amenityResponses, photoResponses, contactResponses, List.of() // hide staff list
			);
		}

		throw new RuntimeException("Invalid authentication state");
	}

	// ------------------ CREATE HELPERS ------------------

	private ContactPerson makeContactFromRequest(ContactPersonRequest req, PgEntity pg) {
		ContactPerson cp = new ContactPerson();
		cp.setName(req.getName());
		cp.setEmail(req.getEmail());
		cp.setPhoneNumber(req.getPhoneNumber());
		cp.setRole(req.getRole());
		cp.setIsPrimary(req.getIsPrimary());
		cp.setPg(pg);
		return cp;
	}

	private Amenity makeAmenityFromRequest(AmenityRequest req, PgEntity pg) {
		Amenity a = new Amenity();
		a.setName(req.getName());
		a.setPg(pg);
		return a;
	}

	private PropertyPhoto makePhotoFromRequest(PropertyPhotoRequest req, PgEntity pg) {
		PropertyPhoto p = new PropertyPhoto();
		p.setImageUrl(req.getImageUrl());
		p.setIsMain(req.getIsMain());
		p.setPg(pg);
		return p;
	}

	// --------------------------------------------------------
	// Apply scalar fields only if present (used by create & update)
	// --------------------------------------------------------
	private void applyScalarFieldsIfPresent(PgRequest request, PgEntity pg, Owner owner) {
		if (owner != null)
			pg.setOwner(owner);

		if (request.getName() != null)
			pg.setName(request.getName());
		if (request.getType() != null)
			pg.setType(request.getType());
		if (request.getPrice() != null)
			pg.setPrice(request.getPrice());
		if (request.getRules() != null)
			pg.setRules(request.getRules());
		if (request.getAvailability() != null)
			pg.setAvailability(request.getAvailability());

		Address address = (pg.getAddress() != null) ? pg.getAddress() : new Address();
		boolean addressTouched = false;
		if (request.getStreet() != null) {
			address.setStreet(request.getStreet());
			addressTouched = true;
		}
		if (request.getCity() != null) {
			address.setCity(request.getCity());
			addressTouched = true;
		}
		if (request.getState() != null) {
			address.setState(request.getState());
			addressTouched = true;
		}
		if (request.getPincode() != null) {
			address.setPincode(request.getPincode());
			addressTouched = true;
		}
		if (addressTouched)
			pg.setAddress(address);
	}

	// --------------------------------------------------------
	// Merge contacts: update existing, add new, delete flagged
	// --------------------------------------------------------
	private void mergeContacts(PgEntity pg, List<ContactPersonRequest> reqContacts) {
		if (reqContacts == null)
			return;
		if (pg.getContacts() == null)
			pg.setContacts(new ArrayList<>());

		for (ContactPersonRequest cReq : reqContacts) {
			if (cReq.getId() != null) {
				ContactPerson existing = contactRepo.findById(cReq.getId())
						.orElseThrow(() -> new RuntimeException("Contact not found: " + cReq.getId()));

				if (existing.getPg() == null || !existing.getPg().getId().equals(pg.getId()))
					throw new RuntimeException("Contact does not belong to this PG: " + cReq.getId());

				if (Boolean.TRUE.equals(cReq.getDelete())) {
					pg.getContacts().removeIf(cp -> cp.getId().equals(existing.getId()));
					contactRepo.delete(existing);
					continue;
				}

				if (cReq.getName() != null)
					existing.setName(cReq.getName());
				if (cReq.getEmail() != null)
					existing.setEmail(cReq.getEmail());
				if (cReq.getPhoneNumber() != null)
					existing.setPhoneNumber(cReq.getPhoneNumber());
				if (cReq.getRole() != null)
					existing.setRole(cReq.getRole());
				if (cReq.getIsPrimary() != null)
					existing.setIsPrimary(cReq.getIsPrimary());

				contactRepo.save(existing);

			} else {
				ContactPerson cp = makeContactFromRequest(cReq, pg);
				ContactPerson saved = contactRepo.save(cp);
				pg.getContacts().add(saved);
			}
		}
	}

	// --------------------------------------------------------
	// Merge amenities: update/add/delete
	// --------------------------------------------------------
	private void mergeAmenities(PgEntity pg, List<AmenityRequest> reqAmenities) {
		if (reqAmenities == null)
			return;
		if (pg.getAmenities() == null)
			pg.setAmenities(new ArrayList<>());

		for (AmenityRequest aReq : reqAmenities) {
			if (aReq.getId() != null) {
				Amenity existing = amenityRepo.findByIdAndPgOwnerId(aReq.getId(), pg.getOwner().getId())
						.orElseThrow(() -> new RuntimeException("Amenity not found or not owned: " + aReq.getId()));

				if (Boolean.TRUE.equals(aReq.getDelete())) {
					pg.getAmenities().removeIf(am -> am.getId().equals(existing.getId()));
					amenityRepo.delete(existing);
					continue;
				}

				if (aReq.getName() != null)
					existing.setName(aReq.getName());
				amenityRepo.save(existing);
			} else {
				Amenity a = makeAmenityFromRequest(aReq, pg);
				Amenity saved = amenityRepo.save(a);
				pg.getAmenities().add(saved);
			}
		}
	}

	// --------------------------------------------------------
	// Merge photos: update/add/delete
	// --------------------------------------------------------
	private void mergePhotos(PgEntity pg, List<PropertyPhotoRequest> reqPhotos) {
		if (reqPhotos == null)
			return;
		if (pg.getPhotos() == null)
			pg.setPhotos(new ArrayList<>());

		for (PropertyPhotoRequest pReq : reqPhotos) {
			if (pReq.getId() != null) {
				PropertyPhoto existing = propertyPhotoRepository
						.findByIdAndPgOwnerId(pReq.getId(), pg.getOwner().getId())
						.orElseThrow(() -> new RuntimeException("Photo not found or not owned: " + pReq.getId()));

				if (Boolean.TRUE.equals(pReq.getDelete())) {
					pg.getPhotos().removeIf(ph -> ph.getId().equals(existing.getId()));
					propertyPhotoRepository.delete(existing);
					continue;
				}

				if (pReq.getImageUrl() != null)
					existing.setImageUrl(pReq.getImageUrl());
				if (pReq.getIsMain() != null)
					existing.setIsMain(pReq.getIsMain());
				propertyPhotoRepository.save(existing);
			} else {
				PropertyPhoto p = makePhotoFromRequest(pReq, pg);
				PropertyPhoto saved = propertyPhotoRepository.save(p);
				pg.getPhotos().add(saved);
			}
		}
	}

	// ------------------------
	// Merge rooms inside a floor
	// ------------------------
	private void mergeRooms(PgEntity pg, Floor floor, List<RoomRequest> reqRooms) {
		if (reqRooms == null)
			return;
		if (floor.getRooms() == null)
			floor.setRooms(new ArrayList<>());

		for (RoomRequest rReq : reqRooms) {

			// --------------------------------------------------------
			// UPDATE EXISTING ROOM
			// --------------------------------------------------------
			if (rReq.getId() != null) {

				RoomEntity existing = roomRepository.findById(rReq.getId())
						.orElseThrow(() -> new RuntimeException("Room not found: " + rReq.getId()));

				if (!existing.getPg().getId().equals(pg.getId()))
					throw new RuntimeException("Room does not belong to this PG");

				// DELETE ROOM
				if (Boolean.TRUE.equals(rReq.getDelete())) {
					floor.getRooms().removeIf(r -> r.getId().equals(existing.getId()));
					roomRepository.delete(existing);
					continue;
				}

				// UPDATE SIMPLE FIELDS
				if (rReq.getRoomNumber() != null)
					existing.setRoomNumber(rReq.getRoomNumber());
				if (rReq.getCapacity() != null)
					existing.setCapacity(rReq.getCapacity());
				if (rReq.getPricePerBed() != null)
					existing.setPricePerBed(rReq.getPricePerBed());
				if (rReq.getAvailable() != null)
					existing.setAvailable(rReq.getAvailable());
				if (rReq.getNotes() != null)
					existing.setNotes(rReq.getNotes());

				// UPDATE FURNITURE (SAFE: clear + addAll)
				if (rReq.getFurniture() != null) {
					if (existing.getFurniture() == null) {
						existing.setFurniture(new ArrayList<>());
					} else {
						existing.getFurniture().clear();
					}
					existing.getFurniture().addAll(rReq.getFurniture());
				}

				// UPDATE AMENITIES (SAFE: clear + repopulate)
				if (rReq.getAmenities() != null) {
					if (existing.getAmenities() == null) {
						existing.setAmenities(new ArrayList<>());
					} else {
						existing.getAmenities().clear();
					}

					for (String a : rReq.getAmenities()) {
						RoomAmenity am = new RoomAmenity();
						am.setAmenityName(a);
						am.setRoom(existing);
						existing.getAmenities().add(am);
					}
				}

				// MOVE ROOM TO NEW FLOOR
				if (rReq.getFloorId() != null && !rReq.getFloorId().equals(floor.getId())) {
					Floor newFloor = floorRepository.findById(rReq.getFloorId())
							.orElseThrow(() -> new RuntimeException("Target floor not found"));

					if (!newFloor.getPg().getId().equals(pg.getId()))
						throw new RuntimeException("Cannot move room to another PG");

					existing.setFloor(newFloor);
				}

				roomRepository.save(existing);
			}

			// --------------------------------------------------------
			// CREATE NEW ROOM
			// --------------------------------------------------------
			else {
				RoomEntity room = new RoomEntity();
				room.setPg(pg);
				room.setFloor(floor);

				if (rReq.getRoomNumber() != null)
					room.setRoomNumber(rReq.getRoomNumber());
				if (rReq.getCapacity() != null)
					room.setCapacity(rReq.getCapacity());
				if (rReq.getPricePerBed() != null)
					room.setPricePerBed(rReq.getPricePerBed());
				if (rReq.getAvailable() != null)
					room.setAvailable(rReq.getAvailable());
				if (rReq.getNotes() != null)
					room.setNotes(rReq.getNotes());

				// FURNITURE
				if (rReq.getFurniture() != null) {
					room.setFurniture(new ArrayList<>(rReq.getFurniture()));
				}

				// AMENITIES
				if (rReq.getAmenities() != null) {
					List<RoomAmenity> ams = new ArrayList<>();
					for (String a : rReq.getAmenities()) {
						RoomAmenity am = new RoomAmenity();
						am.setAmenityName(a);
						am.setRoom(room);
						ams.add(am);
					}
					room.setAmenities(ams);
				}

				RoomEntity saved = roomRepository.save(room);
				floor.getRooms().add(saved);
			}
		}
	}

	// ------------------------
	// Merge floors (top-level)
	// ------------------------
	private void mergeFloors(PgEntity pg, List<FloorRequest> reqFloors) {
		if (reqFloors == null)
			return;
		if (pg.getFloors() == null)
			pg.setFloors(new ArrayList<>());

		for (FloorRequest fReq : reqFloors) {
			if (fReq.getId() != null) {
				Floor existing = floorRepository.findById(fReq.getId())
						.orElseThrow(() -> new RuntimeException("Floor not found: " + fReq.getId()));

				if (existing.getPg() == null || !existing.getPg().getId().equals(pg.getId()))
					throw new RuntimeException("Floor does not belong to this PG: " + fReq.getId());

				if (Boolean.TRUE.equals(fReq.getDelete())) {
					// delete nested rooms first
					if (existing.getRooms() != null) {
						existing.getRooms().forEach(r -> roomRepository.delete(r));
					}
					pg.getFloors().removeIf(fl -> fl.getId().equals(existing.getId()));
					floorRepository.delete(existing);
					continue;
				}

				if (fReq.getFloorName() != null)
					existing.setFloorName(fReq.getFloorName());
				if (fReq.getTotalRooms() != null)
					existing.setTotalRooms(fReq.getTotalRooms());
				if (fReq.getCommonAreas() != null)
					existing.setCommonAreas(fReq.getCommonAreas());

				mergeRooms(pg, existing, fReq.getRooms());
				floorRepository.save(existing);

			} else {
				Floor floor = new Floor();
				floor.setPg(pg);
				if (fReq.getFloorName() != null)
					floor.setFloorName(fReq.getFloorName());
				if (fReq.getTotalRooms() != null)
					floor.setTotalRooms(fReq.getTotalRooms());
				if (fReq.getCommonAreas() != null)
					floor.setCommonAreas(fReq.getCommonAreas());

				Floor savedFloor = floorRepository.save(floor);

				if (fReq.getRooms() != null && !fReq.getRooms().isEmpty()) {
					List<RoomEntity> rooms = new ArrayList<>();
					for (RoomRequest rReq : fReq.getRooms()) {
						RoomEntity room = new RoomEntity();
						room.setPg(pg);
						room.setFloor(savedFloor);

						if (rReq.getRoomNumber() != null)
							room.setRoomNumber(rReq.getRoomNumber());
						if (rReq.getCapacity() != null)
							room.setCapacity(rReq.getCapacity());
						if (rReq.getPricePerBed() != null)
							room.setPricePerBed(rReq.getPricePerBed());
						if (rReq.getAvailable() != null)
							room.setAvailable(rReq.getAvailable());
						if (rReq.getNotes() != null)
							room.setNotes(rReq.getNotes());
						if (rReq.getFurniture() != null)
							room.setFurniture(rReq.getFurniture());

						if (rReq.getAmenities() != null) {
							List<RoomAmenity> amList = rReq.getAmenities().stream().map(a -> {
								RoomAmenity am = new RoomAmenity();
								am.setAmenityName(a);
								am.setRoom(room);
								return am;
							}).collect(Collectors.toList());
							room.setAmenities(amList);
						}

						RoomEntity savedRoom = roomRepository.save(room);
						rooms.add(savedRoom);
					}
					savedFloor.setRooms(rooms);
					floorRepository.save(savedFloor);
				}

				pg.getFloors().add(savedFloor);
			}
		}
	}

	// ------------------ SECURITY HELPERS ------------------

	private Long getLoggedInOwnerId() {
		Long id = SecurityUtils.getLoggedInOwnerId();
		if (id == null)
			throw new RuntimeException("Unauthorized: Owner not found in token");
		return id;
	}

	// ------------------ GET ALL PGs ------------------

	@Override
	public List<PgResponse> getAllPgs() {

		Long ownerId = SecurityUtils.getLoggedInOwnerId();
		Long staffPgId = SecurityUtils.getStaffPgId();

		if (ownerId != null) {
			return pgRepository.findByOwnerId(ownerId).stream().map(this::toPgResponse).collect(Collectors.toList());
		}

		if (staffPgId != null) {
			PgEntity pg = pgRepository.findById(staffPgId).orElseThrow(() -> new RuntimeException("PG not found"));
			return List.of(toPgResponse(pg));
		}

		throw new RuntimeException("Unauthorized access");
	}

	// ------------------ GET PG BY ID ------------------

	@Override
	public PgResponse getPgById(Long id) {
		Long ownerId = SecurityUtils.getLoggedInOwnerId();
		Long staffPgId = SecurityUtils.getStaffPgId();

		if (ownerId != null) {
			PgEntity pg = pgRepository.findByIdAndOwnerId(id, ownerId)
					.orElseThrow(() -> new RuntimeException("PG not found OR not owned by you"));
			return toPgResponse(pg);
		}

		if (staffPgId != null && staffPgId.equals(id)) {
			PgEntity pg = pgRepository.findById(id).orElseThrow(() -> new RuntimeException("PG not found"));
			return toPgResponse(pg);
		}

		throw new RuntimeException("Unauthorized PG access");
	}

	// ------------------ CREATE PG ------------------

	@Override
	@Transactional
	public PgResponse createPg(PgRequest request) {

		Long ownerId = getLoggedInOwnerId();

		Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Owner not found"));

		PgEntity pg = new PgEntity();

		// apply scalar fields
		applyScalarFieldsIfPresent(request, pg, owner);

		// Save once — but do NOT overwrite pg variable used in lambdas
		PgEntity savedPg = pgRepository.save(pg);

		// contacts
		if (request.getContacts() != null && !request.getContacts().isEmpty()) {
			List<ContactPerson> contacts = request.getContacts().stream()
					.map(req -> makeContactFromRequest(req, savedPg)).map(contactRepo::save)
					.collect(Collectors.toList());
			savedPg.setContacts(contacts);
		}

		// amenities
		if (request.getAmenities() != null && !request.getAmenities().isEmpty()) {
			List<Amenity> list = request.getAmenities().stream().map(req -> makeAmenityFromRequest(req, savedPg))
					.map(amenityRepo::save).collect(Collectors.toList());
			savedPg.setAmenities(list);
		}

		// photos
		if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
			List<PropertyPhoto> list = request.getPhotos().stream().map(req -> makePhotoFromRequest(req, savedPg))
					.map(propertyPhotoRepository::save).collect(Collectors.toList());
			savedPg.setPhotos(list);
		}

		// floors + nested rooms
		if (request.getFloors() != null && !request.getFloors().isEmpty()) {
			List<Floor> savedFloors = new ArrayList<>();
			for (FloorRequest fReq : request.getFloors()) {
				Floor floor = new Floor();
				floor.setPg(savedPg);
				if (fReq.getFloorName() != null)
					floor.setFloorName(fReq.getFloorName());
				if (fReq.getTotalRooms() != null)
					floor.setTotalRooms(fReq.getTotalRooms());
				if (fReq.getCommonAreas() != null)
					floor.setCommonAreas(fReq.getCommonAreas());

				Floor savedFloor = floorRepository.save(floor);

				if (fReq.getRooms() != null && !fReq.getRooms().isEmpty()) {
					List<RoomEntity> rooms = new ArrayList<>();
					for (RoomRequest rReq : fReq.getRooms()) {
						RoomEntity room = new RoomEntity();
						room.setPg(savedPg);
						room.setFloor(savedFloor);

						if (rReq.getRoomNumber() != null)
							room.setRoomNumber(rReq.getRoomNumber());
						if (rReq.getCapacity() != null)
							room.setCapacity(rReq.getCapacity());
						if (rReq.getPricePerBed() != null)
							room.setPricePerBed(rReq.getPricePerBed());
						if (rReq.getAvailable() != null)
							room.setAvailable(rReq.getAvailable());
						if (rReq.getNotes() != null)
							room.setNotes(rReq.getNotes());
						if (rReq.getFurniture() != null)
							room.setFurniture(rReq.getFurniture());

						if (rReq.getAmenities() != null) {
							List<RoomAmenity> amList = rReq.getAmenities().stream().map(a -> {
								RoomAmenity am = new RoomAmenity();
								am.setAmenityName(a);
								am.setRoom(room);
								return am;
							}).collect(Collectors.toList());
							room.setAmenities(amList);
						}

						RoomEntity savedRoom = roomRepository.save(room);
						rooms.add(savedRoom);
					}
					savedFloor.setRooms(rooms);
					floorRepository.save(savedFloor);
				}

				savedFloors.add(savedFloor);
			}
			savedPg.setFloors(savedFloors);
		}

		// final save with children attached
		PgEntity finalSaved = pgRepository.save(savedPg);
		return toPgResponse(finalSaved);
	}

	// ------------------ UPDATE PG ------------------

	@Override
	@Transactional
	public PgResponse updatePg(Long id, PgRequest request) {

		Long ownerId = getLoggedInOwnerId();

		PgEntity existing = pgRepository.findByIdAndOwnerId(id, ownerId)
				.orElseThrow(() -> new RuntimeException("PG not found OR not owned by you: " + id));

		Owner owner = existing.getOwner();

		// 1) Scalars & address (partial)
		applyScalarFieldsIfPresent(request, existing, owner);

		// 2) Merge nested lists
		mergeContacts(existing, request.getContacts());
		mergeAmenities(existing, request.getAmenities());
		mergePhotos(existing, request.getPhotos());

		// 3) Merge floors + rooms
		mergeFloors(existing, request.getFloors());

		PgEntity updated = pgRepository.save(existing);

		return toPgResponse(updated);
	}

	// ------------------ DELETE PG ------------------

	@Override
	public void deletePg(Long id) {
		Long ownerId = getLoggedInOwnerId();

		PgEntity pg = pgRepository.findByIdAndOwnerId(id, ownerId)
				.orElseThrow(() -> new RuntimeException("PG not found OR not owned by you: " + id));

		pgRepository.delete(pg);
	}

}
