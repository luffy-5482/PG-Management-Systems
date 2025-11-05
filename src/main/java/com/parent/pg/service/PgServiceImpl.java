package com.parent.pg.service;

import java.util.List;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.pg.model.PgEntity;
import com.parent.pg.repository.PgRepository;

@Service
public class PgServiceImpl implements PgService {

    @Autowired
    private PgRepository pgRepository;

    @Override
    public List<PgEntity> getAllPgs() {
        return pgRepository.findAll();
    }

    @Override
    public PgEntity getPgById(Long id) {
        return pgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PG not found with id: " + id));
    }

    @Override
    public PgEntity createPg(PgEntity pg) {
        return pgRepository.save(pg);
    }

    @Override
    public PgEntity updatePg(Long id, PgEntity pgDetails) {
        PgEntity pg = getPgById(id);
        pg.setName(pgDetails.getName());
        pg.setType(pgDetails.getType());
        pg.setPrice(pgDetails.getPrice());
        pg.setRules(pgDetails.getRules());
        pg.setAvailability(pgDetails.getAvailability());
        pg.setAddress(pgDetails.getAddress());

        // Optional updates (only if you allow updating relationships)
        if (pgDetails.getAmenities() != null)
            pg.setAmenities(pgDetails.getAmenities());

        if (pgDetails.getPhotos() != null)
            pg.setPhotos(pgDetails.getPhotos());

        return pgRepository.save(pg);
    }

    @Override
    public void deletePg(Long id) {
        pgRepository.deleteById(id);
    }
}
