package com.parent.owner.service;

import java.util.List;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @Override
    public Owner getOwnerById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + id));
    }

    @Override
    public Owner createOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public Owner updateOwner(Long id, Owner ownerDetails) {
        Owner owner = getOwnerById(id);
        owner.setFullName(ownerDetails.getFullName());
        owner.setEmail(ownerDetails.getEmail());
        owner.setPhoneNumber(ownerDetails.getPhoneNumber());
        owner.setGender(ownerDetails.getGender());
        owner.setPassword(ownerDetails.getPassword());
        return ownerRepository.save(owner);
    }

    @Override
    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }
}
