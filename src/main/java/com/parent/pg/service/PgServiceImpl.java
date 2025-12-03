package com.parent.pg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.parent.config.JwtService;
import com.parent.config.SecurityUtils;
import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;
import com.parent.pg.dto.AmenityRequest;
import com.parent.pg.dto.AmenityResponse;
import com.parent.pg.dto.ContactPersonRequest;
import com.parent.pg.dto.ContactPersonResponse;
import com.parent.pg.dto.FloorRequest;
import com.parent.pg.dto.FloorResponse;
import com.parent.pg.dto.PgRequest;
import com.parent.pg.dto.PgResponse;
import com.parent.pg.dto.PropertyPhotoRequest;
import com.parent.pg.dto.PropertyPhotoResponse;
import com.parent.pg.dto.RoomRequest;
import com.parent.pg.dto.RoomResponse;
import com.parent.pg.model.Address;
import com.parent.pg.model.Amenity;
import com.parent.pg.model.ContactPerson;
import com.parent.pg.model.Floor;
import com.parent.pg.model.PgEntity;
import com.parent.pg.model.PropertyPhoto;
import com.parent.pg.model.RoomAmenity;
import com.parent.pg.model.RoomEntity;
import com.parent.pg.repository.AminityRepo;
import com.parent.pg.repository.ContactPersonRepository;
import com.parent.pg.repository.FloorRepository;
import com.parent.pg.repository.PgRepository;
import com.parent.pg.repository.PropertyPhotoRepository;
import com.parent.pg.repository.RoomRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PgServiceImpl implements PgService {

	@Autowired
	private PgRepository pgRepository;

	@Autowired
	private OwnerRepository ownerRepository;

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

	@Autowired
	private JwtService jwtService; // ⭐ REQUIRED FOR MANAGER PG RESTRICTION



	// ---------------------- MAPPERS ----------------------

	private AmenityResponse toAmenityResponse(Amenity amenity) {
		return new AmenityResponse(
				amenity.getId(),
				amenity.getName(),
				(amenity.getPg() != null ? amenity.getPg().getId() : null));
	}

	private PropertyPhotoResponse toPhotoResponse(PropertyPhoto photo) {
		return new PropertyPhotoResponse(
				photo.getId(),
				photo.getImageUrl(),
				photo.getIsMain(),
				(photo.getPg() != null ? photo.getPg().getId() : null));
	}

	private RoomResponse toRoomResponse(RoomEntity room) {

		List<String> amenityNames = (room.getAmenities() == null) ? List.of()
				: room.getAmenities().stream().map(RoomAmenity::getAmenityName).collect(Collectors.toList());

		return new RoomResponse(
				room.getId(),
				room.getRoomNumber(),
				room.getCapacity(),
				room.getPricePerBed(),
				room.getAvailable(),
				room.getNotes(),
				amenityNames,
				room.getFurniture(),
				(room.getFloor() != null ? room.getFloor().getId() : null),
				(room.getPg() != null ? room.getPg().getId() : null));
	}

	private FloorResponse toFloorResponse(Floor floor) {
		if (floor == null)
			return null;

		int totalRooms = (floor.getTotalRooms() != null) ? floor.getTotalRooms() : 0;

		List<RoomResponse> roomResponses = (floor.getRooms() == null) ? List.of()
				: floor.getRooms().stream().map(this::toRoomResponse).collect(Collectors.toList());

		return new FloorResponse(
				floor.getId(),
				floor.getFloorName(),
				totalRooms,
				floor.getCommonAreas(),
				(floor.getPg() != null ? floor.getPg().getId() : null),
				roomResponses);
	}

	private ContactPersonResponse toContactResponse(ContactPerson c) {
		return new ContactPersonResponse(
				c.getId(),
				c.getName(),
				c.getEmail(),
				c.getPhoneNumber(),
				c.getRole(),
				c.getIsPrimary(),
				(c.getPg() != null ? c.getPg().getId() : null));
	}

	// ---------------------- PG → Response ----------------------

	private PgResponse toPgResponse(PgEntity pg) {

		Long ownerId = SecurityUtils.getLoggedInOwnerId();
		boolean isOwner = ownerId != null;

		List<FloorResponse> floors = (pg.getFloors() == null) ? List.of()
				: pg.getFloors().stream().map(this::toFloorResponse).collect(Collectors.toList());

		List<AmenityResponse> amenities = (pg.getAmenities() == null) ? List.of()
				: pg.getAmenities().stream().map(this::toAmenityResponse).collect(Collectors.toList());

		List<PropertyPhotoResponse> photos = (pg.getPhotos() == null) ? List.of()
				: pg.getPhotos().stream().map(this::toPhotoResponse).collect(Collectors.toList());

		List<ContactPersonResponse> contacts = (pg.getContacts() == null) ? List.of()
				: pg.getContacts().stream().map(this::toContactResponse).collect(Collectors.toList());

		Long oId = pg.getOwner() != null ? pg.getOwner().getId() : null;
		String oName = pg.getOwner() != null ? pg.getOwner().getFullName() : null;
		String oEmail = pg.getOwner() != null ? pg.getOwner().getEmail() : null;

		return new PgResponse(
				pg.getId(),
				pg.getName(),
				pg.getType(),
				pg.getPrice(),
				pg.getRules(),
				pg.getAvailability(),
				pg.getAddress(),
				oId,
				oName,
				oEmail,
				floors,
				amenities,
				photos,
				contacts);
	}

	// ---------------------- SECURITY HELPERS ----------------------

	private Long getLoggedInOwnerId() {
		Long id = SecurityUtils.getLoggedInOwnerId();
		if (id == null)
			throw new RuntimeException("Unauthorized: Owner not found in token");
		return id;
	}

	private Set<Long> extractAllowedPgIds(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || !token.startsWith("Bearer "))
			return Set.of();
		token = token.substring(7);
		return jwtService.extractAllowedPgIdsFromToken(token);
	}

	// ---------------------- GET ALL PGs ----------------------

	@Override
	public List<PgResponse> getAllPgs() {

		Long ownerId = SecurityUtils.getLoggedInOwnerId();

		if (ownerId != null) {
			return pgRepository.findByOwnerId(ownerId)
					.stream()
					.map(this::toPgResponse)
					.collect(Collectors.toList());
		}

		throw new RuntimeException("Unauthorized access");
	}

	// ---------------------- GET PG BY ID (OWNER or MANAGER restricted) ----------------------

	@Override
	public PgResponse getPgById(Long id, HttpServletRequest request) {

	    Long ownerId = SecurityUtils.getLoggedInOwnerId();
	    Long managerId = (Long) request.getAttribute("managerId");

	    // ----------------------------------------------------
	    // OWNER → Full access but only to own PGs
	    // ----------------------------------------------------
	    if (ownerId != null) {
	        PgEntity pg = pgRepository.findByIdAndOwnerId(id, ownerId)
	                .orElseThrow(() -> new RuntimeException("PG not found OR not owned by you"));
	        return toPgResponse(pg);
	    }

	    // ----------------------------------------------------
	    // MANAGER → Only PGs assigned in token
	    // ----------------------------------------------------
	    if (managerId != null) {

	        // extract allowed PG IDs from JWT token
	        Set<Long> allowed = jwtService.extractAllowedPgIdsFromRequest(request);

	        if (allowed == null || !allowed.contains(id)) {
	            throw new RuntimeException("Unauthorized: Manager cannot access this PG");
	        }

	        // Now fetch PG normally
	        PgEntity pg = pgRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("PG not found"));

	        return toPgResponse(pg);
	    }

	    // neither owner nor manager
	    throw new RuntimeException("Unauthorized PG access");
	}


	@Override
	public List<PgResponse> getPgsByIds(Set<Long> ids) {
		return pgRepository.findAllById(ids)
				.stream()
				.map(this::toPgResponse)
				.collect(Collectors.toList());
	}

	// ---------------------- CREATE PG ----------------------

	@Override
	@Transactional
	public PgResponse createPg(PgRequest request) {

		Long ownerId = getLoggedInOwnerId();

		Owner owner = ownerRepository.findById(ownerId)
				.orElseThrow(() -> new RuntimeException("Owner not found"));

		PgEntity pg = new PgEntity();

		applyScalar(request, pg, owner);

		PgEntity savedPg = pgRepository.save(pg);

		if (request.getContacts() != null) {
			List<ContactPerson> contacts = request.getContacts().stream()
					.map(req -> makeContact(req, savedPg))
					.map(contactRepo::save)
					.collect(Collectors.toList());
			savedPg.setContacts(contacts);
		}

		if (request.getAmenities() != null) {
			List<Amenity> list = request.getAmenities().stream()
					.map(req -> makeAmenity(req, savedPg))
					.map(amenityRepo::save)
					.collect(Collectors.toList());
			savedPg.setAmenities(list);
		}

		if (request.getPhotos() != null) {
			List<PropertyPhoto> list = request.getPhotos().stream()
					.map(req -> makePhoto(req, savedPg))
					.map(propertyPhotoRepository::save)
					.collect(Collectors.toList());
			savedPg.setPhotos(list);
		}

		if (request.getFloors() != null) {
			List<Floor> savedFloors = new ArrayList<>();
			for (FloorRequest fReq : request.getFloors()) {
				Floor floor = new Floor();
				floor.setPg(savedPg);
				floor.setFloorName(fReq.getFloorName());
				floor.setTotalRooms(fReq.getTotalRooms());
				floor.setCommonAreas(fReq.getCommonAreas());

				Floor savedFloor = floorRepository.save(floor);

				if (fReq.getRooms() != null) {
					List<RoomEntity> rooms = new ArrayList<>();

					for (RoomRequest rReq : fReq.getRooms()) {
						RoomEntity room = new RoomEntity();
						room.setPg(savedPg);
						room.setFloor(savedFloor);

						room.setRoomNumber(rReq.getRoomNumber());
						room.setCapacity(rReq.getCapacity());
						room.setPricePerBed(rReq.getPricePerBed());
						room.setAvailable(rReq.getAvailable());
						room.setNotes(rReq.getNotes());
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

						rooms.add(roomRepository.save(room));
					}

					savedFloor.setRooms(rooms);
					floorRepository.save(savedFloor);
				}

				savedFloors.add(savedFloor);
			}
			savedPg.setFloors(savedFloors);
		}

		return toPgResponse(savedPg);
	}

	// ---------------------- UPDATE PG ----------------------

	@Override
	@Transactional
	public PgResponse updatePg(Long id, PgRequest request) {

		Long ownerId = getLoggedInOwnerId();

		PgEntity existing = pgRepository.findByIdAndOwnerId(id, ownerId)
				.orElseThrow(() -> new RuntimeException("PG not found OR not owned by you: " + id));

		Owner owner = existing.getOwner();

		applyScalar(request, existing, owner);

		mergeContacts(existing, request.getContacts());
		mergeAmenities(existing, request.getAmenities());
		mergePhotos(existing, request.getPhotos());
		mergeFloors(existing, request.getFloors());

		return toPgResponse(pgRepository.save(existing));
	}

	// ---------------------- DELETE PG ----------------------

	@Override
	public void deletePg(Long id) {

		Long ownerId = getLoggedInOwnerId();

		PgEntity pg = pgRepository.findByIdAndOwnerId(id, ownerId)
				.orElseThrow(() -> new RuntimeException("PG not found OR not owned by you: " + id));

		pgRepository.delete(pg);
	}



	// ---------------------- INTERNAL HELPERS ----------------------

	private void applyScalar(PgRequest req, PgEntity pg, Owner owner) {

		if (owner != null)
			pg.setOwner(owner);

		if (req.getName() != null)
			pg.setName(req.getName());

		if (req.getType() != null)
			pg.setType(req.getType());

		if (req.getPrice() != null)
			pg.setPrice(req.getPrice());

		if (req.getRules() != null)
			pg.setRules(req.getRules());

		if (req.getAvailability() != null)
			pg.setAvailability(req.getAvailability());

		Address address = (pg.getAddress() != null) ? pg.getAddress() : new Address();
		boolean touched = false;

		if (req.getStreet() != null) {
			address.setStreet(req.getStreet());
			touched = true;
		}
		if (req.getCity() != null) {
			address.setCity(req.getCity());
			touched = true;
		}
		if (req.getState() != null) {
			address.setState(req.getState());
			touched = true;
		}
		if (req.getPincode() != null) {
			address.setPincode(req.getPincode());
			touched = true;
		}

		if (touched)
			pg.setAddress(address);
	}

	private ContactPerson makeContact(ContactPersonRequest req, PgEntity pg) {
		ContactPerson cp = new ContactPerson();
		cp.setName(req.getName());
		cp.setEmail(req.getEmail());
		cp.setPhoneNumber(req.getPhoneNumber());
		cp.setRole(req.getRole());
		cp.setIsPrimary(req.getIsPrimary());
		cp.setPg(pg);
		return cp;
	}

	private Amenity makeAmenity(AmenityRequest req, PgEntity pg) {
		Amenity a = new Amenity();
		a.setName(req.getName());
		a.setPg(pg);
		return a;
	}

	private PropertyPhoto makePhoto(PropertyPhotoRequest req, PgEntity pg) {
		PropertyPhoto p = new PropertyPhoto();
		p.setImageUrl(req.getImageUrl());
		p.setIsMain(req.getIsMain());
		p.setPg(pg);
		return p;
	}

	private void mergeContacts(PgEntity pg, List<ContactPersonRequest> req) {
		if (req == null)
			return;

		if (pg.getContacts() == null)
			pg.setContacts(new ArrayList<>());

		for (ContactPersonRequest cReq : req) {
			if (cReq.getId() != null) {
				ContactPerson existing = contactRepo.findById(cReq.getId())
						.orElseThrow(() -> new RuntimeException("Contact not found"));

				if (Boolean.TRUE.equals(cReq.getDelete())) {
					pg.getContacts().removeIf(x -> x.getId().equals(existing.getId()));
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
				ContactPerson newContact = makeContact(cReq, pg);
				ContactPerson saved = contactRepo.save(newContact);
				pg.getContacts().add(saved);
			}
		}
	}

	private void mergeAmenities(PgEntity pg, List<AmenityRequest> req) {

		if (req == null)
			return;

		if (pg.getAmenities() == null)
			pg.setAmenities(new ArrayList<>());

		for (AmenityRequest aReq : req) {
			if (aReq.getId() != null) {

				Amenity existing = amenityRepo.findById(aReq.getId())
						.orElseThrow(() -> new RuntimeException("Amenity not found"));

				if (Boolean.TRUE.equals(aReq.getDelete())) {
					pg.getAmenities().removeIf(x -> x.getId().equals(existing.getId()));
					amenityRepo.delete(existing);
					continue;
				}

				if (aReq.getName() != null)
					existing.setName(aReq.getName());

				amenityRepo.save(existing);

			} else {
				Amenity a = makeAmenity(aReq, pg);
				Amenity saved = amenityRepo.save(a);
				pg.getAmenities().add(saved);
			}
		}
	}

	private void mergePhotos(PgEntity pg, List<PropertyPhotoRequest> req) {
		if (req == null)
			return;

		if (pg.getPhotos() == null)
			pg.setPhotos(new ArrayList<>());

		for (PropertyPhotoRequest pReq : req) {
			if (pReq.getId() != null) {
				PropertyPhoto existing = propertyPhotoRepository.findById(pReq.getId())
						.orElseThrow(() -> new RuntimeException("Photo not found"));

				if (Boolean.TRUE.equals(pReq.getDelete())) {
					pg.getPhotos().removeIf(x -> x.getId().equals(existing.getId()));
					propertyPhotoRepository.delete(existing);
					continue;
				}

				if (pReq.getImageUrl() != null)
					existing.setImageUrl(pReq.getImageUrl());
				if (pReq.getIsMain() != null)
					existing.setIsMain(pReq.getIsMain());

				propertyPhotoRepository.save(existing);

			} else {
				PropertyPhoto p = makePhoto(pReq, pg);
				PropertyPhoto saved = propertyPhotoRepository.save(p);
				pg.getPhotos().add(saved);
			}
		}
	}

	private void mergeFloors(PgEntity pg, List<FloorRequest> reqFloors) {

		if (reqFloors == null)
			return;

		if (pg.getFloors() == null)
			pg.setFloors(new ArrayList<>());

		for (FloorRequest fReq : reqFloors) {

			if (fReq.getId() != null) {

				Floor existing = floorRepository.findById(fReq.getId())
						.orElseThrow(() -> new RuntimeException("Floor not found"));

				if (Boolean.TRUE.equals(fReq.getDelete())) {

					if (existing.getRooms() != null)
						existing.getRooms().forEach(r -> roomRepository.delete(r));

					pg.getFloors().removeIf(x -> x.getId().equals(existing.getId()));
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
				floor.setFloorName(fReq.getFloorName());
				floor.setTotalRooms(fReq.getTotalRooms());
				floor.setCommonAreas(fReq.getCommonAreas());

				Floor savedFloor = floorRepository.save(floor);

				if (fReq.getRooms() != null) {

					List<RoomEntity> rooms = new ArrayList<>();

					for (RoomRequest rReq : fReq.getRooms()) {

						RoomEntity room = new RoomEntity();
						room.setPg(pg);
						room.setFloor(savedFloor);

						room.setRoomNumber(rReq.getRoomNumber());
						room.setCapacity(rReq.getCapacity());
						room.setPricePerBed(rReq.getPricePerBed());
						room.setAvailable(rReq.getAvailable());
						room.setNotes(rReq.getNotes());
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

						rooms.add(roomRepository.save(room));
					}

					savedFloor.setRooms(rooms);
					floorRepository.save(savedFloor);
				}

				pg.getFloors().add(savedFloor);
			}
		}
	}

	private void mergeRooms(PgEntity pg, Floor floor, List<RoomRequest> reqRooms) {

		if (reqRooms == null)
			return;

		if (floor.getRooms() == null)
			floor.setRooms(new ArrayList<>());

		for (RoomRequest rReq : reqRooms) {

			if (rReq.getId() != null) {

				RoomEntity existing = roomRepository.findById(rReq.getId())
						.orElseThrow(() -> new RuntimeException("Room not found"));

				if (Boolean.TRUE.equals(rReq.getDelete())) {
					floor.getRooms().removeIf(x -> x.getId().equals(existing.getId()));
					roomRepository.delete(existing);
					continue;
				}

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

				if (rReq.getFurniture() != null) {
					existing.getFurniture().clear();
					existing.getFurniture().addAll(rReq.getFurniture());
				}

				if (rReq.getAmenities() != null) {
					existing.getAmenities().clear();
					for (String a : rReq.getAmenities()) {
						RoomAmenity am = new RoomAmenity();
						am.setAmenityName(a);
						am.setRoom(existing);
						existing.getAmenities().add(am);
					}
				}

				if (rReq.getFloorId() != null && !rReq.getFloorId().equals(floor.getId())) {
					Floor newFloor = floorRepository.findById(rReq.getFloorId())
							.orElseThrow(() -> new RuntimeException("Target floor not found"));

					if (!newFloor.getPg().getId().equals(pg.getId()))
						throw new RuntimeException("Cannot move room to another PG");

					existing.setFloor(newFloor);
				}

				roomRepository.save(existing);

			} else {

				RoomEntity room = new RoomEntity();
				room.setPg(pg);
				room.setFloor(floor);

				room.setRoomNumber(rReq.getRoomNumber());
				room.setCapacity(rReq.getCapacity());
				room.setPricePerBed(rReq.getPricePerBed());
				room.setAvailable(rReq.getAvailable());
				room.setNotes(rReq.getNotes());
				room.setFurniture(rReq.getFurniture());

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

				RoomEntity savedRoom = roomRepository.save(room);
				floor.getRooms().add(savedRoom);
			}
		}
	}
	@Override
	public PgResponse getPgById(Long id) {
	    // call the role-aware version with the current request
	    HttpServletRequest request =
	            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
	                    .getRequest();

	    return getPgById(id, request);
	}

	


}
