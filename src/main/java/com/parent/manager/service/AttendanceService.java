package com.parent.manager.service;

import com.parent.manager.model.Attendance;
import com.parent.manager.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository repo;

    public AttendanceService(AttendanceRepository repo) { this.repo = repo; }

    public Attendance markCheckIn(Attendance a) {
        return repo.save(a);
    }

    public Attendance markCheckOut(Long id, java.time.LocalTime out) {
        Attendance a = repo.findById(id).orElseThrow();
        a.setCheckOut(out);
        return repo.save(a);
    }

    public List<Attendance> getByManagerAndDate(Long managerId, LocalDate date) {
        return repo.findByManagerIdAndDate(managerId, date);
    }
}
