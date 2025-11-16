package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;
import com.parent.pg.dto.*;
import com.parent.pg.model.*;
import com.parent.pg.repository.PgRepository;

@Service
public class PgServiceImpl implements PgService {

    @Autowired private PgRepository pgRepository;
    @Autowired private OwnerRepository ownerRepository;

    // ------------------ MAPPERS ------------------

    private AmenityResponse toAmenityResponse(Amenity amenity) {
        return new AmenityResponse(
                amenity.getId(),
                amenity.getName(),
                (amenity.getPg() != null ? amenity.getPg().getId() : null)
        );
    }

    private PropertyPhotoResponse toPhotoResponse(PropertyPhoto photo) {
        return new PropertyPhotoResponse(
                photo.getId(),
                photo.getImageUrl(),
                photo.getIsMain(),
                (photo.getPg() != null ? photo.getPg().getId() : null)
        );
    }

    private RoomResponse toRoomResponse(RoomEntity room) {

        // NEW: Convert List<RoomAmenity> → List<String>
        List<String> amenityNames = (room.getAmenities() == null)
                ? List.of()
                : room.getAmenities()
                      .stream()
                      .map(RoomAmenity::getAmenityName)
                      .collect(Collectors.toList());

        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getCapacity(),
                room.getPricePerBed(),
                room.getAvailable(),
                room.getNotes(),
                amenityNames,   // 🔥 replaced old room.getAmenities()
                room.getFurniture(),
                (room.getFloor() != null ? room.getFloor().getId() : null),
                (room.getPg() != null ? room.getPg().getId() : null)
        );
    }


    private FloorResponse toFloorResponse(Floor floor) {
        if (floor == null) return null;

        int totalRooms = (floor.getTotalRooms() != null) ? floor.getTotalRooms() : 0;

        List<RoomResponse> roomResponses = (floor.getRooms() == null)
                ? List.of()
                : floor.getRooms().stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());

        return new FloorResponse(
                floor.getId(),
                floor.getFloorName(),
                totalRooms,
                floor.getCommonAreas(),
                (floor.getPg() != null ? floor.getPg().getId() : null),
                roomResponses
        );
    }

    private ContactPersonResponse toContactResponse(ContactPerson c) {
        return new ContactPersonResponse(
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPhoneNumber(),
                c.getRole(),
                c.getIsPrimary(),
                (c.getPg() != null ? c.getPg().getId() : null)
        );
    }

    private PgResponse toPgResponse(PgEntity pg) {

        List<FloorResponse> floorResponses = (pg.getFloors() == null)
                ? List.of()
                : pg.getFloors().stream()
                .map(this::toFloorResponse)
                .collect(Collectors.toList());

        List<AmenityResponse> amenityResponses = (pg.getAmenities() == null)
                ? List.of()
                : pg.getAmenities().stream()
                .map(this::toAmenityResponse)
                .collect(Collectors.toList());

        List<PropertyPhotoResponse> photoResponses = (pg.getPhotos() == null)
                ? List.of()
                : pg.getPhotos().stream()
                .map(this::toPhotoResponse)
                .collect(Collectors.toList());

        List<ContactPersonResponse> contactResponses = (pg.getContacts() == null)
                ? List.of()
                : pg.getContacts().stream()
                .map(this::toContactResponse)
                .collect(Collectors.toList());

        return new PgResponse(
                pg.getId(),
                pg.getName(),
                pg.getType(),
                pg.getPrice(),
                pg.getRules(),
                pg.getAvailability(),
                pg.getAddress(),
                (pg.getOwner() != null ? pg.getOwner().getId() : null),
                (pg.getOwner() != null ? pg.getOwner().getFullName() : null),
                (pg.getOwner() != null ? pg.getOwner().getEmail() : null),
                floorResponses,
                amenityResponses,
                photoResponses,
                contactResponses
        );
    }

    // ------------------ APPLY REQUEST ------------------

    private void applyPgRequest(PgRequest request, PgEntity pg, Owner owner) {

        pg.setOwner(owner);
        pg.setName(request.getName());
        pg.setType(request.getType());
        pg.setPrice(request.getPrice());
        pg.setRules(request.getRules());
        pg.setAvailability(request.getAvailability());

        Address address = (pg.getAddress() != null) ? pg.getAddress() : new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        pg.setAddress(address);

        // -------- CONTACTS MAPPING --------
        if (request.getContacts() != null) {

            List<ContactPerson> list = request.getContacts()
                .stream()
                .map(req -> {
                    ContactPerson cp = new ContactPerson();
                    cp.setName(req.getName());
                    cp.setEmail(req.getEmail());
                    cp.setPhoneNumber(req.getPhoneNumber());
                    cp.setRole(req.getRole());
                    cp.setIsPrimary(req.getIsPrimary());
                    cp.setPg(pg);
                    return cp;
                })
                .collect(Collectors.toList());

            pg.setContacts(list);
        }
    }

    // ------------------ SECURITY ------------------

    private Long getLoggedInOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null)
            throw new RuntimeException("Unauthorized: Owner not found in token");
        return id;
    }

    // ------------------ GET ALL PGs ------------------

    @Override
    public List<PgResponse> getAllPgs() {
        Long ownerId = getLoggedInOwnerId();

        return pgRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::toPgResponse)
                .collect(Collectors.toList());
    }

    // ------------------ GET PG BY ID ------------------

    @Override
    public PgResponse getPgById(Long id) {
        Long ownerId = getLoggedInOwnerId();

        PgEntity pg = pgRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("PG not found OR not owned by you: " + id));

        return toPgResponse(pg);
    }

    // ------------------ CREATE PG ------------------

    @Override
    public PgResponse createPg(PgRequest request) {

        Long ownerId = getLoggedInOwnerId();

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        PgEntity pg = new PgEntity();

        applyPgRequest(request, pg, owner);

        PgEntity saved = pgRepository.save(pg);

        return toPgResponse(saved);
    }

    // ------------------ UPDATE PG ------------------

    @Override
    public PgResponse updatePg(Long id, PgRequest request) {

        Long ownerId = getLoggedInOwnerId();

        PgEntity existing = pgRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("PG not found OR not owned by you: " + id));

        Owner owner = existing.getOwner();

        applyPgRequest(request, existing, owner);

        PgEntity updated = pgRepository.save(existing);

        return toPgResponse(updated);
    }

    // ------------------ DELETE PG ------------------

    @Override
    public void deletePg(Long id) {
        Long ownerId = getLoggedInOwnerId();

        PgEntity pg = pgRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() ->
                        new RuntimeException("PG not found OR not owned by you: " + id));

        pgRepository.delete(pg);
    }
}
