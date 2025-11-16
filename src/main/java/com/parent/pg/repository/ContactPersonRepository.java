package com.parent.pg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.pg.model.ContactPerson;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    List<ContactPerson> findByPg_Id(Long pgId);
}
