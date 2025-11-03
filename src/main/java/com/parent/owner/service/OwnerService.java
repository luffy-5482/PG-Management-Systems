package com.parent.owner.service;

import java.util.List;

import com.parent.owner.model.Owner;

public interface OwnerService {
    List<Owner> getAllOwners();
    Owner getOwnerById(Long id);
    Owner createOwner(Owner owner);
    Owner updateOwner(Long id, Owner owner);
    void deleteOwner(Long id);
}
