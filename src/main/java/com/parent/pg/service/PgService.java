package com.parent.pg.service;

import java.util.List;	

import com.parent.pg.model.PgEntity;

public interface PgService {
	List<PgEntity> getAllPgs();

	PgEntity getPgById(Long id);

	PgEntity createPg(PgEntity pg);

	PgEntity updatePg(Long id, PgEntity pg);

	void deletePg(Long id);
}

