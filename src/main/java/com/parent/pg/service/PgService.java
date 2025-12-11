package com.parent.pg.service;

import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import com.parent.pg.dto.PgRequest;
import com.parent.pg.dto.PgResponse;

public interface PgService {

    // OWNER: list all PGs
    List<PgResponse> getAllPgs();
    PgResponse getPgById(Long id); // used by owner service

    // NEW: Role aware (Owner OR Manager)
    PgResponse getPgById(Long id, HttpServletRequest request);

    // NEW: Manager → fetch allowed PGs only
    List<PgResponse> getPgsByIds(Set<Long> ids);

    PgResponse createPg(PgRequest pgRequest);
    PgResponse updatePg(Long id, PgRequest pgRequest);
    void deletePg(Long id);
}
