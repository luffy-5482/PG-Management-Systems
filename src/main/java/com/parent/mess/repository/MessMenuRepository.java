package com.parent.mess.repository;

import com.parent.mess.model.MessMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MessMenuRepository extends JpaRepository<MessMenu, LocalDate> {

    // find for one date (JpaRepository already has findById)
    // find menus in range
    List<MessMenu> findByMenuDateBetweenOrderByMenuDateAsc(LocalDate from, LocalDate to);

    // delete older than date
    void deleteByMenuDateBefore(LocalDate date);
}
