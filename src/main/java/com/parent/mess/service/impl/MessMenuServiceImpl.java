package com.parent.mess.service.impl;

import com.parent.mess.dto.MessMenuDto;
import com.parent.mess.model.MessMenu;
import com.parent.mess.repository.MessMenuRepository;
import com.parent.mess.service.MessMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessMenuServiceImpl implements MessMenuService {

    private final MessMenuRepository repo;

    public MessMenuServiceImpl(MessMenuRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public MessMenuDto createOrUpdateMenu(MessMenuDto dto) {
        LocalDate date = dto.getMenuDate();
        if (date == null) throw new RuntimeException("menuDate is required");

        MessMenu entity = repo.findById(date).orElseGet(() -> {
            MessMenu m = new MessMenu();
            m.setMenuDate(date);
            return m;
        });

        entity.setBreakfast(dto.getBreakfast());
        entity.setLunch(dto.getLunch());
        entity.setDinner(dto.getDinner());
        entity.setNotes(dto.getNotes());

        MessMenu saved = repo.save(entity);
        return toDto(saved);
    }

    @Override
    public MessMenuDto getMenuByDate(LocalDate date) {
        return repo.findById(date).map(this::toDto).orElse(null);
    }

    @Override
    public List<MessMenuDto> getMenusForRange(LocalDate from, LocalDate to) {
        return repo.findByMenuDateBetweenOrderByMenuDateAsc(from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMenu(LocalDate date) {
        repo.deleteById(date);
    }

    @Override
    @Transactional
    public void deleteMenusBefore(LocalDate date) {
        repo.deleteByMenuDateBefore(date);
    }

    private MessMenuDto toDto(MessMenu m) {
        MessMenuDto d = new MessMenuDto();
        d.setMenuDate(m.getMenuDate());
        d.setBreakfast(m.getBreakfast());
        d.setLunch(m.getLunch());
        d.setDinner(m.getDinner());
        d.setNotes(m.getNotes());
        return d;
    }
}
