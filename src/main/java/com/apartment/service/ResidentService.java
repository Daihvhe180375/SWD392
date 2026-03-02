package com.apartment.service;

import com.apartment.model.Resident;
import java.util.Optional;

/**
 * Interface định nghĩa nghiệp vụ liên quan đến Resident (cư dân).
 */
public interface ResidentService {

    /**
     * Tìm thông tin Resident theo user_id.
     * 
     * @return Optional chứa Resident nếu có, rỗng nếu không
     */
    Optional<Resident> findByUserId(Integer userId);
}
