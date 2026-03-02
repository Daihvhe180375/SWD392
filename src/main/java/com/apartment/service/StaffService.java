package com.apartment.service;

import com.apartment.dto.StaffCreateDto;
import com.apartment.model.Staff;

import java.util.List;

public interface StaffService {
    void addStaff(StaffCreateDto dto);

    List<Staff> getAllStaff();

    java.util.Optional<Staff> findByUserId(Integer userId);
}
