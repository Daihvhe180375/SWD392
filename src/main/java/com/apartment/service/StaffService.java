package com.apartment.service;

import com.apartment.model.Staff;
import java.util.Optional;

/**
 * Interface định nghĩa nghiệp vụ liên quan đến Staff (nhân viên).
 */
public interface StaffService {

    /**
     * Tìm thông tin Staff theo user_id.
     */
    Optional<Staff> findByUserId(Integer userId);
}
