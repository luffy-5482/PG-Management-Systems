package com.parent.pg.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.parent.pg.model.Floor;
import com.parent.pg.model.PgEntity;
import com.parent.pg.repository.FloorRepository;
import com.parent.pg.repository.PgRepository;

@Service
public class FloorServiceImpl implements FloorService {

	@Autowired
	private FloorRepository floorRepository;

	@Autowired
	private PgRepository pgRepository;

	@Override
	public List<Floor> getFloorsByPgId(Long pgId) {
		return floorRepository.findByPg_Id(pgId);
	}

	@Override
	public Floor createFloor(Long pgId, Floor floor) {
		PgEntity pg = pgRepository.findById(pgId)
				.orElseThrow(() -> new RuntimeException("PG not found with id: " + pgId));
		floor.setPg(pg);
		// if rooms are provided inside the floor payload, make sure to set floor on
		// each room
		if (floor.getRooms() != null) {
			floor.getRooms().forEach(r -> r.setFloor(floor));
			// also ensure each room links to the PG
			floor.getRooms().forEach(r -> r.setPg(pg));
		}
		return floorRepository.save(floor);
	}

	@Override
	public Floor updateFloor(Long floorId, Floor floorPayload) {
		Floor existing = floorRepository.findById(floorId)
				.orElseThrow(() -> new RuntimeException("Floor not found with id: " + floorId));
		existing.setFloorName(floorPayload.getFloorName());
		existing.setTotalRooms(floorPayload.getTotalRooms());
		existing.setCommonAreas(floorPayload.getCommonAreas());
		// optional: update rooms if provided (careful with orphanRemoval)
		return floorRepository.save(existing);
	}

	@Override
	public void deleteFloor(Long floorId) {
		floorRepository.deleteById(floorId);
	}
}
