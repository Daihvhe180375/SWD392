package com.apartment.service.impl;

import com.apartment.model.Staff;
import com.apartment.repository.StaffRepository;
import com.apartment.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Cài đặt StaffService - truy vấn thông tin nhân viên.
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public Optional<Staff> findByUserId(Integer userId) {
        return staffRepository.findByUserUserId(userId);
    }
}
