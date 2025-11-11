package com.parent.pg.service;

import java.util.List;		
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;
import com.parent.pg.dto.*;
import com.parent.pg.model.*;
import com.parent.pg.repository.*;

@Service
public class PgServiceImpl implements PgService {

    @Autowired private PgRepository pgRepository;
    @Autowired private OwnerRepository ownerRepository;

    // --------------------------
    // 🔹 Utility Mappers
    // --------------------------
  
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
        return new RoomResponse(
            room.getId(),
            room.getRoomNumber(), 
            room.getCapacity(),
            room.getPricePerBed(),
            room.getAvailable(),
            room.getNotes(),
            room.getAmenities(),
            room.getFurniture(),
            (room.getFloor() != null ? room.getFloor().getId() : null),
            (room.getPg() != null ? room.getPg().getId() : null)
        );
    }

    private FloorResponse toFloorResponse(Floor floor) {
        if (floor == null) return null;

        // null-safe defaults
        int totalRooms = (floor.getTotalRooms() != null) ? floor.getTotalRooms() : 0;

        List<RoomResponse> roomResponses = (floor.getRooms() == null)
                ? List.of()
                : floor.getRooms().stream().map(this::toRoomResponse).collect(Collectors.toList());

        return new FloorResponse(
            floor.getId(),
            floor.getFloorName(),           // can be null; fine for JSON
            totalRooms,                     // ✅ never null now
            floor.getCommonAreas(),         // can be null; fine for JSON
            (floor.getPg() != null ? floor.getPg().getId() : null),
            roomResponses
        );
    }
 

    private PgResponse toPgResponse(PgEntity pg) {
        List<FloorResponse> floorResponses = (pg.getFloors() == null) ? List.of()
                : pg.getFloors().stream().map(this::toFloorResponse).collect(Collectors.toList());

        List<AmenityResponse> amenityResponses = (pg.getAmenities() == null) ? List.of()
                : pg.getAmenities().stream().map(this::toAmenityResponse).collect(Collectors.toList());

        List<PropertyPhotoResponse> photoResponses = (pg.getPhotos() == null) ? List.of()
                : pg.getPhotos().stream().map(this::toPhotoResponse).collect(Collectors.toList());

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
            photoResponses
        );
    }

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
    }

    // --------------------------
    // 🔹 CRUD Implementations 
    // --------------------------

    @Override
    public List<PgResponse> getAllPgs() {
        return pgRepository.findAll().stream().map(this::toPgResponse).collect(Collectors.toList());
    }

    @Override
    public PgResponse getPgById(Long id) {
        PgEntity pg = pgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + id));
        return toPgResponse(pg);
    }

    @Override
    public PgResponse createPg(PgRequest request) {
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + request.getOwnerId()));

        PgEntity pg = new PgEntity();
        applyPgRequest(request, pg, owner);
        PgEntity saved = pgRepository.save(pg);

        // Optional: initialize empty lists
        saved.setAmenities(List.of());
        saved.setPhotos(List.of());
        saved.setFloors(List.of());

        return toPgResponse(saved);
    }

    @Override
    public PgResponse updatePg(Long id, PgRequest request) {
        PgEntity existing = pgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + id));

        Owner owner = existing.getOwner();
        if (request.getOwnerId() != null) {
            owner = ownerRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found with id: " + request.getOwnerId()));
        }

        applyPgRequest(request, existing, owner);
        PgEntity updated = pgRepository.save(existing);

        return toPgResponse(updated);
    }

    @Override
    public void deletePg(Long id) {
        pgRepository.deleteById(id);
    }
}
