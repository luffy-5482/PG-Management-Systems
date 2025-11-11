package com.parent.owner.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.owner.dto.OwnerRequest;
import com.parent.owner.dto.OwnerResponse;
import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;
import com.parent.pg.dto.PgResponse;
import com.parent.pg.model.PgEntity;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired private OwnerRepository ownerRepository;
    @Autowired private com.parent.pg.service.PgService pgService; // use mapper logic from PgServiceImpl

    private OwnerResponse toOwnerRes(Owner o) {
        List<PgResponse> pgList = (o.getPgs() == null) ? List.of()
                : o.getPgs().stream()
                    .map(pg -> pgService.getPgById(pg.getId())) // reuse PgService mapping to include floors/rooms/etc.
                    .collect(Collectors.toList());

        return new OwnerResponse(
            o.getId(), o.getFullName(), o.getEmail(), o.getPhoneNumber(), o.getGender(), pgList
        );
    }

    private void applyRequest(OwnerRequest r, Owner o) {
        o.setFullName(r.getFullName());
        o.setEmail(r.getEmail());
        o.setPhoneNumber(r.getPhoneNumber());
        o.setGender(r.getGender());
        o.setPassword(r.getPassword());
    }

    @Override
    public List<OwnerResponse> getAllOwners() {
        return ownerRepository.findAll().stream().map(this::toOwnerRes).collect(Collectors.toList());
    }

    @Override
    public OwnerResponse getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + id));
        return toOwnerRes(owner);
    }

    @Override
    public OwnerResponse createOwner(OwnerRequest request) {
        Owner o = new Owner();
        applyRequest(request, o);
        Owner saved = ownerRepository.save(o);
        return toOwnerRes(saved);
    }

    @Override
    public OwnerResponse updateOwner(Long id, OwnerRequest request) {
        Owner o = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + id));
        applyRequest(request, o);
        Owner saved = ownerRepository.save(o);
        return toOwnerRes(saved);
    }

    @Override
    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }
}
