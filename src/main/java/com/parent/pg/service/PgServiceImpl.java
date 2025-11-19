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
import com.parent.staff.dto.StaffResponse;

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
                amenityNames,
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

    // ------------------ PG → Response (OWNER FULL / STAFF LIMITED) ------------------

    private PgResponse toPgResponse(PgEntity pg) {

        Long ownerId   = SecurityUtils.getLoggedInOwnerId();
        Long staffId   = SecurityUtils.getLoggedInStaffId();

        boolean isOwner = ownerId != null;
        boolean isStaff = staffId != null;

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

        if (isOwner) {

            List<StaffResponse> staffResponses = (pg.getStaff() == null)
                    ? List.of()
                    : pg.getStaff().stream()
                            .map(staff -> {
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
                            })
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
                    contactResponses,
                    staffResponses
            );
        }

        if (isStaff) {

            return new PgResponse(
                    pg.getId(),
                    pg.getName(),
                    pg.getType(),
                    null,                               // staff cannot see price
                    pg.getRules(),
                    pg.getAvailability(),
                    pg.getAddress(),
                    null,                               // hide ownerId
                    null,                               // hide ownerName
                    null,                               // hide ownerEmail
                    floorResponses,
                    amenityResponses,
                    photoResponses,
                    contactResponses,
                    List.of()                           // hide staff list
            );
        }

        throw new RuntimeException("Invalid authentication state");
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

        // OWNER: return all his PGs
        if (ownerId != null) {
            return pgRepository.findByOwnerId(ownerId)
                    .stream()
                    .map(this::toPgResponse)
                    .collect(Collectors.toList());
        }

        // STAFF: return only THEIR PG
        if (staffPgId != null) {
            PgEntity pg = pgRepository.findById(staffPgId)
                    .orElseThrow(() -> new RuntimeException("PG not found"));

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
            PgEntity pg = pgRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("PG not found"));
            return toPgResponse(pg);
        }

        throw new RuntimeException("Unauthorized PG access");
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
