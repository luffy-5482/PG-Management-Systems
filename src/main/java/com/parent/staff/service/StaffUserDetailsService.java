package com.parent.staff.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.parent.staff.model.StaffEntity;
import com.parent.staff.repository.StaffRepository;

@Service
public class StaffUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;

    public StaffUserDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<StaffEntity> staff = staffRepository.findByEmail(email);

        if (staff.isEmpty()) {
            throw new UsernameNotFoundException("Staff not found");
        }

        return new StaffUserDetails(staff.get());
    }
}
