package com.apartment.service.impl;

import com.apartment.model.Resident;
import com.apartment.repository.ResidentRepository;
import com.apartment.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Cài đặt ResidentService - truy vấn thông tin cư dân.
 */
@Service
public class ResidentServiceImpl implements ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    @Override
    public Optional<Resident> findByUserId(Integer userId) {
        return residentRepository.findByUserUserId(userId);
    }
}
