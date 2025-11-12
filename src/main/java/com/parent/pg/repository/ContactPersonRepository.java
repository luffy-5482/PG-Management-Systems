package com.parent.pg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.pg.model.ContactPerson;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    ContactPerson findByPgId(Long pgId);
}
