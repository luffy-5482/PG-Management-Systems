package com.parent.mess.service;

import com.parent.mess.dto.MessMenuDto;

import java.time.LocalDate;
import java.util.List;

public interface MessMenuService {

    MessMenuDto createOrUpdateMenu(MessMenuDto dto);
    MessMenuDto getMenuByDate(LocalDate date);
    List<MessMenuDto> getMenusForRange(LocalDate from, LocalDate to);
    void deleteMenu(LocalDate date); // owner-only
    void deleteMenusBefore(LocalDate date); // cleanup helper
}
