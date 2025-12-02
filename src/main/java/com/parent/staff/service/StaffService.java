package com.parent.staff.service;

import java.util.List;

import com.parent.staff.dto.StaffRequest;
import com.parent.staff.dto.StaffResponse;

public interface StaffService {
    StaffResponse createStaff(StaffRequest request);
    StaffResponse updateStaff(Long id, StaffRequest request);
    void deleteStaff(Long id);
    StaffResponse getStaff(Long id);
    List<StaffResponse> getStaffByPg(Long pgId);
}
