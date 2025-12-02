package com.parent.staff.service;

import com.parent.staff.dto.StaffLoginRequest;
import com.parent.staff.dto.StaffLoginResponse;

public interface StaffAuthService {
    StaffLoginResponse login(StaffLoginRequest request);
}
