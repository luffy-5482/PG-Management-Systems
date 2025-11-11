package com.parent.pg.service;

import java.util.List;	
import com.parent.pg.dto.PgRequest;
import com.parent.pg.dto.PgResponse;

public interface PgService {
    List<PgResponse> getAllPgs();
    PgResponse getPgById(Long id);
    PgResponse createPg(PgRequest pgRequest);
    PgResponse updatePg(Long id, PgRequest pgRequest);
    void deletePg(Long id);
}
